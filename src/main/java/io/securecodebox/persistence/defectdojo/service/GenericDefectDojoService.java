// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import io.securecodebox.persistence.defectdojo.exception.TooManyResponsesException;
import io.securecodebox.persistence.defectdojo.http.AuthHeaderFactory;
import io.securecodebox.persistence.defectdojo.http.RestTemplateFactory;
import io.securecodebox.persistence.defectdojo.http.ProxyConfig;
import io.securecodebox.persistence.defectdojo.http.ProxyConfigFactory;
import io.securecodebox.persistence.defectdojo.model.Model;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
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
import java.util.*;

/**
 * Generic base implementation with common functionality shared by services
 *
 * @param <T> type of model the service handles
 */
@Slf4j
abstract class GenericDefectDojoService<T extends Model> implements DefectDojoService<T> {
  private static final long DEFECT_DOJO_OBJET_LIMIT = 100L;
  private final ClientConfig clientConfig;
  private final ProxyConfig proxyConfig;
  private final RestTemplate restTemplate;
  private final Mappers mapper = new Mappers();

  /**
   * Convenience constructor which initializes {@link #proxyConfig}
   *
   * @param clientConfig not {@code null}
   */
  public GenericDefectDojoService(ClientConfig clientConfig) {
    this(clientConfig, new ProxyConfigFactory().create());
  }

  /**
   * Dedicated constructor
   *
   * @param clientConfig not {@code null}
   * @param proxyConfig  not {@code null}
   */
  public GenericDefectDojoService(@NonNull ClientConfig clientConfig, @NonNull ProxyConfig proxyConfig) {
    super();
    this.clientConfig = clientConfig;
    this.proxyConfig = proxyConfig;
    this.restTemplate = setupRestTemplate();
  }

  @Override
  public final T get(long id) {
    final HttpEntity<String> payload = new HttpEntity<>(createAuthorizationHeaders());

    final var url = createBaseUrl().resolve(String.valueOf(id));
    log.debug("Requesting URL: {}", url);
    final ResponseEntity<T> response = restTemplate.exchange(
      url,
      HttpMethod.GET,
      payload,
      getModelClass()
    );

    return response.getBody();
  }

  @Override
  public final List<T> search() {
    return search(Collections.emptyMap());
  }

  @Override
  public final List<T> search(@NonNull Map<String, Object> queryParams) {
    final List<T> objects = new LinkedList<>();

    boolean hasNext;
    long page = 0;
    do {
      final var response = internalSearch(queryParams, DEFECT_DOJO_OBJET_LIMIT, DEFECT_DOJO_OBJET_LIMIT * page++);
      objects.addAll(response.getResults());
      hasNext = response.getNext() != null;

      if (page > this.clientConfig.getMaxPageCountForGets()) {
        final var msg = String.format(
          "Found too many response object. Quitting after %d paginated API pages of %d each.",
          page - 1,
          DEFECT_DOJO_OBJET_LIMIT
        );
        throw new TooManyResponsesException(msg);
      }
    } while (hasNext);

    return objects;
  }

  @Override
  @SuppressWarnings("unchecked")
  public final Optional<T> searchUnique(@NonNull T searchObject) {
    final Map<String, Object> queryParams = mapper.searchStringMapper()
      .convertValue(searchObject, Map.class);
    final var objects = search(queryParams);

    return objects.stream()
      .filter(object -> object != null && object.equalsQueryString(queryParams))
      .findFirst();
  }

  @Override
  public final Optional<T> searchUnique(@NonNull Map<String, Object> queryParams) {
    final var found = search(queryParams);

    return found.stream()
      .filter(object -> object.equalsQueryString(queryParams))
      .findFirst();
  }

  @Override
  public final T create(@NonNull T object) {
    final HttpEntity<T> payload = new HttpEntity<>(object, createAuthorizationHeaders());
    final ResponseEntity<T> response = restTemplate.exchange(createBaseUrl(), HttpMethod.POST, payload, getModelClass());

    return response.getBody();
  }

  @Override
  public final void delete(long id) {
    final HttpEntity<String> payload = new HttpEntity<>(createAuthorizationHeaders());

    final var url = createBaseUrl().resolve(id + "/");
    restTemplate.exchange(url, HttpMethod.DELETE, payload, String.class);
  }

  @Override
  public final T update(@NonNull T object, long id) {
    final HttpEntity<T> payload = new HttpEntity<>(object, createAuthorizationHeaders());
    final var url = createBaseUrl().resolve(id + "/");
    final ResponseEntity<T> response = restTemplate.exchange(url, HttpMethod.PUT, payload, getModelClass());

    return response.getBody();
  }

  /**
   * Get the URL path for the REST endpoint relative to {@link ClientConfig#API_PREFIX}
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

  final URI createBaseUrl() {
    final var buffer = clientConfig.getUrl() +
      ClientConfig.API_PREFIX +
      getUrlPath() +
      '/';

    return URI.create(buffer).normalize();
  }

  final ObjectMapper modelObjectMapper() {
    // We only expose this mapper to subclasses.
    return mapper.modelObjectMapper();
  }

  private HttpHeaders createAuthorizationHeaders() {
    final var factory = new AuthHeaderFactory(clientConfig);
    factory.setProxyConfig(proxyConfig);
    return factory.generateAuthorizationHeaders();
  }

  private RestTemplate setupRestTemplate() {
    final RestTemplate template = new RestTemplateFactory(new ProxyConfigFactory().create()).createRestTemplate();
    // TODO: Maybe all of this could be moved into the factory.
    final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(mapper.modelObjectMapper());
    template.setMessageConverters(List.of(
      new FormHttpMessageConverter(),
      new ResourceHttpMessageConverter(),
      new StringHttpMessageConverter(),
      converter
    ));
    return template;
  }

  protected PaginatedResult<T> internalSearch(Map<String, Object> queryParams, long limit, long offset) {
    final HttpEntity<String> payload = new HttpEntity<>(createAuthorizationHeaders());

    final var mutableQueryParams = new HashMap<>(queryParams);
    mutableQueryParams.put("limit", String.valueOf(limit));
    mutableQueryParams.put("offset", String.valueOf(offset));

    final var multiValueMap = new LinkedMultiValueMap<String, String>();
    for (var entry : mutableQueryParams.entrySet()) {
      multiValueMap.set(entry.getKey(), String.valueOf(entry.getValue()));
    }

    final var url = createBaseUrl();
    final UriComponentsBuilder builder;
    builder = UriComponentsBuilder
      .fromUri(url)
      .queryParams(multiValueMap);

    final ResponseEntity<String> responseString = restTemplate.exchange(
      builder.build(mutableQueryParams),
      HttpMethod.GET,
      payload,
      String.class
    );

    return deserializeList(responseString.getBody());
  }
}
