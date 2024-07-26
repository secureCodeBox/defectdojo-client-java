// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.cfg.CoercionAction;
import com.fasterxml.jackson.databind.cfg.CoercionInputShape;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.exception.LoopException;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

// TODO: Remove JsonProcessingException, URISyntaxException from public API and use a own runtime exception type bc these checked exceptions clutter the client coe.
@Slf4j
abstract class GenericDefectDojoService<T extends Model> implements DefectDojoService<T> {
  private static final String API_PREFIX = "/api/v2/";
  private static final long DEFECT_DOJO_OBJET_LIMIT = 100L;
  protected Config config;

  protected ObjectMapper objectMapper;
  protected ObjectMapper searchStringMapper;

  @Getter
  protected RestTemplate restTemplate;

  public GenericDefectDojoService(@NonNull Config config) {
    super();
    this.config = config;

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

    final var url = this.config.getUrl() + API_PREFIX + this.getUrlPath() + "/" + id;
    log.debug("Requesting URL: {}", url);
    try {
      ResponseEntity<T> response = restTemplate.exchange(
        url,
        HttpMethod.GET,
        payload,
        getModelClass()
      );

      return response.getBody();
    } catch (RestClientException e) {
      log.error("Exception while getting data: {}", e.getMessage());
      throw new PersistenceException("Failed to get data.", e);
    }
  }

  @Override
  public final List<T> search(@NonNull Map<String, Object> queryParams) throws URISyntaxException, JsonProcessingException {
    List<T> objects = new LinkedList<>();

    boolean hasNext;
    long page = 0;
    do {
      var response = internalSearch(queryParams, DEFECT_DOJO_OBJET_LIMIT, DEFECT_DOJO_OBJET_LIMIT * page++);
      objects.addAll(response.getResults());

      hasNext = response.getNext() != null;
      if (page > this.config.getMaxPageCountForGets()) {
        throw new LoopException("Found too many response object. Quitting after " + (page - 1) + " paginated API pages of " + DEFECT_DOJO_OBJET_LIMIT + " each.");
      }
    } while (hasNext);

    return objects;
  }

  @Override
  public final List<T> search() throws URISyntaxException, JsonProcessingException {
    return search(new LinkedHashMap<>());
  }

  @Override
  @SuppressWarnings("unchecked")
  public final Optional<T> searchUnique(T searchObject) throws URISyntaxException, JsonProcessingException {
    Map<String, Object> queryParams = searchStringMapper.convertValue(searchObject, Map.class);

    var objects = search(queryParams);

    return objects.stream()
      .filter(object -> object != null && object.equalsQueryString(queryParams))
      .findFirst();
  }

  @Override
  public final Optional<T> searchUnique(@NonNull Map<String, Object> queryParams) throws URISyntaxException, JsonProcessingException {
    var objects = search(queryParams);

    return objects.stream()
      .filter(object -> object.equalsQueryString(queryParams))
      .findFirst();
  }

  @Override
  public final T create(@NonNull T object) {
    var restTemplate = this.getRestTemplate();
    HttpEntity<T> payload = new HttpEntity<>(object, getDefectDojoAuthorizationHeaders());

    ResponseEntity<T> response = restTemplate.exchange(this.config.getUrl() + API_PREFIX + getUrlPath() + "/", HttpMethod.POST, payload, getModelClass());
    return response.getBody();
  }

  @Override
  public final void delete(long id) {
    var restTemplate = this.getRestTemplate();
    HttpEntity<String> payload = new HttpEntity<>(getDefectDojoAuthorizationHeaders());

    restTemplate.exchange(this.config.getUrl() + API_PREFIX + getUrlPath() + "/" + id + "/", HttpMethod.DELETE, payload, String.class);
  }

  @Override
  public final T update(@NonNull T object, long id) {
    var restTemplate = this.getRestTemplate();
    HttpEntity<T> payload = new HttpEntity<>(object, getDefectDojoAuthorizationHeaders());

    ResponseEntity<T> response = restTemplate.exchange(this.config.getUrl() + API_PREFIX + getUrlPath() + "/" + id + "/", HttpMethod.PUT, payload, getModelClass());
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
   * @throws JsonProcessingException if string is not parsable as JSON
   */
  protected abstract PaginatedResult<T> deserializeList(@NonNull String response) throws JsonProcessingException;

  /**
   * @return The DefectDojo Authentication Header
   */
  private HttpHeaders getDefectDojoAuthorizationHeaders() {
    return new Foo(config, new ProxyConfigFactory().create()).generateAuthorizationHeaders();
  }

  private RestTemplate setupRestTemplate() {
    RestTemplate restTemplate = new Foo(config, new ProxyConfigFactory().create()).createRestTemplate();
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

  protected PaginatedResult<T> internalSearch(Map<String, Object> queryParams, long limit, long offset) throws JsonProcessingException, URISyntaxException {
    var restTemplate = this.getRestTemplate();
    HttpEntity<String> payload = new HttpEntity<>(getDefectDojoAuthorizationHeaders());

    var mutableQueryParams = new HashMap<>(queryParams);

    mutableQueryParams.put("limit", String.valueOf(limit));
    mutableQueryParams.put("offset", String.valueOf(offset));

    var multiValueMap = new LinkedMultiValueMap<String, String>();
    for (var entry : mutableQueryParams.entrySet()) {
      multiValueMap.set(entry.getKey(), String.valueOf(entry.getValue()));
    }

    var url = new URI(this.config.getUrl() + API_PREFIX + this.getUrlPath() + "/");
    log.debug("Requesting URL: {}", url);
    var uriBuilder = UriComponentsBuilder.fromUri(url).queryParams(multiValueMap);

    ResponseEntity<String> responseString = restTemplate.exchange(
      uriBuilder.build(mutableQueryParams),
      HttpMethod.GET,
      payload,
      String.class
    );

    return deserializeList(responseString.getBody());
  }
}
