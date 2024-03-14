// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.exception.TooManyResponsesException;
import io.securecodebox.persistence.defectdojo.http.AuthHeaderFactory;
import io.securecodebox.persistence.defectdojo.http.Foo;
import io.securecodebox.persistence.defectdojo.http.ProxyConfigFactory;
import io.securecodebox.persistence.defectdojo.model.Engagement;
import io.securecodebox.persistence.defectdojo.model.Model;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Slf4j
abstract class GenericDefectDojoService<T extends Model> implements DefectDojoService<T> {
  private static final String API_PREFIX = "/api/v2/";
  private static final long DEFECT_DOJO_OBJET_LIMIT = 100L;
  protected ClientConfig clientConfig;

  protected ObjectMapper objectMapper;
  protected ObjectMapper searchStringMapper;

  @Getter
  protected RestTemplate restTemplate;

  public GenericDefectDojoService(@NonNull ClientConfig clientConfig) {
    this.clientConfig = clientConfig;

    this.objectMapper = new ObjectMapper();
    this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.objectMapper.coercionConfigFor(Engagement.Status.class).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
    this.objectMapper.findAndRegisterModules();

    this.searchStringMapper = new ObjectMapper();
    this.searchStringMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.searchStringMapper.coercionConfigFor(Engagement.Status.class).setCoercion(CoercionInputShape.EmptyString, CoercionAction.AsNull);
    this.searchStringMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);

    this.restTemplate = this.setupRestTemplate();
  }

  @Override
  public final T get(long id) {
    var restTemplate = this.getRestTemplate();
    HttpEntity<String> payload = new HttpEntity<>(getDefectDojoAuthorizationHeaders());

    final var url = createBaseUrl() + id;
    log.debug("Requesting URL: {}", url);
    ResponseEntity<T> response = restTemplate.exchange(
      url,
      HttpMethod.GET,
      payload,
      getModelClass()
    );

    return response.getBody();
  }

  @Override
  public final List<T> search(@NonNull Map<String, Object> queryParams) {
    List<T> objects = new LinkedList<>();

    boolean hasNext;
    long page = 0;
    do {
      var response = internalSearch(queryParams, DEFECT_DOJO_OBJET_LIMIT, DEFECT_DOJO_OBJET_LIMIT * page++);
      objects.addAll(response.getResults());

      hasNext = response.getNext() != null;
      if (page > this.clientConfig.getMaxPageCountForGets()) {
        throw new TooManyResponsesException("Found too many response object. Quitting after " + (page - 1) + " paginated API pages of " + DEFECT_DOJO_OBJET_LIMIT + " each.");
      }
    } while (hasNext);

    return objects;
  }

  @Override
  public final List<T> search() {
    return search(Collections.emptyMap());
  }

  @Override
  @SuppressWarnings("unchecked")
  public final Optional<T> searchUnique(@NonNull T searchObject) {
    Map<String, Object> queryParams = searchStringMapper.convertValue(searchObject, Map.class);

    var objects = search(queryParams);

    return objects.stream()
      .filter(object -> object != null && object.equalsQueryString(queryParams))
      .findFirst();
  }

  @Override
  public final Optional<T> searchUnique(@NonNull Map<String, Object> queryParams) {
    var found = search(queryParams);

    return found.stream()
      .filter(object -> object.equalsQueryString(queryParams))
      .findFirst();
  }

  @Override
  public final T create(@NonNull T object) {
    var restTemplate = this.getRestTemplate();
    HttpEntity<T> payload = new HttpEntity<>(object, getDefectDojoAuthorizationHeaders());

    ResponseEntity<T> response = restTemplate.exchange(createBaseUrl(), HttpMethod.POST, payload, getModelClass());
    return response.getBody();
  }

  @Override
  public final void delete(long id) {
    var restTemplate = this.getRestTemplate();
    HttpEntity<String> payload = new HttpEntity<>(getDefectDojoAuthorizationHeaders());

    restTemplate.exchange(createBaseUrl() + id + "/", HttpMethod.DELETE, payload, String.class);
  }

  @Override
  public final T update(@NonNull T object, long id) {
    var restTemplate = this.getRestTemplate();
    HttpEntity<T> payload = new HttpEntity<>(object, getDefectDojoAuthorizationHeaders());

    ResponseEntity<T> response = restTemplate.exchange(createBaseUrl() + id + "/", HttpMethod.PUT, payload, getModelClass());
    return response.getBody();
  }
  
  /**
   * Get the URL path for the REST endpoint relative to {@link #API_PREFIX}
   *
   * @return not {@code null}, not empty
   */
  protected abstract String getUrlPath();

  /**
   * Get the model type the service handles
   *
   * @return not {@code null}
   */
  protected abstract Class<T> getModelClass();

  /**
   * Deserializes the response JSON string
   *
   * @param response not {@code null}, maybe empty
   * @return not {@code null}
   */
  protected abstract PaginatedResult<T> deserializeList(@NonNull String response);

  private String createBaseUrl() {
    return this.clientConfig.getUrl() + API_PREFIX + getUrlPath() + "/";
  }

  /**
   * @return The DefectDojo Authentication Header
   */
  private HttpHeaders getDefectDojoAuthorizationHeaders() {
    final var factory = new AuthHeaderFactory(clientConfig);
    factory.setProxyConfig(new ProxyConfigFactory().create());
    return factory.generateAuthorizationHeaders();
  }

  private RestTemplate setupRestTemplate() {
    RestTemplate restTemplate = new Foo(new ProxyConfigFactory().create()).createRestTemplate();
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(this.objectMapper);
    restTemplate.setMessageConverters(List.of(
      new FormHttpMessageConverter(),
      new ResourceHttpMessageConverter(),
      new StringHttpMessageConverter(),
      converter
    ));
    return restTemplate;
  }

  protected PaginatedResult<T> internalSearch(Map<String, Object> queryParams, long limit, long offset) {
    var restTemplate = this.getRestTemplate();
    HttpEntity<String> payload = new HttpEntity<>(getDefectDojoAuthorizationHeaders());

    var mutableQueryParams = new HashMap<>(queryParams);

    mutableQueryParams.put("limit", String.valueOf(limit));
    mutableQueryParams.put("offset", String.valueOf(offset));

    var multiValueMap = new LinkedMultiValueMap<String, String>();
    for (var entry : mutableQueryParams.entrySet()) {
      multiValueMap.set(entry.getKey(), String.valueOf(entry.getValue()));
    }

    final var url = createBaseUrl();
    final UriComponentsBuilder builder;
    try {
      builder = UriComponentsBuilder
        .fromUri(new URI(url))
        .queryParams(multiValueMap);
    } catch (URISyntaxException e) {
      throw new PersistenceException("Bad URL given: " + url, e);
    }

    final ResponseEntity<String> responseString = restTemplate.exchange(
      builder.build(mutableQueryParams),
      HttpMethod.GET,
      payload,
      String.class
    );

    return deserializeList(responseString.getBody());
  }
}
