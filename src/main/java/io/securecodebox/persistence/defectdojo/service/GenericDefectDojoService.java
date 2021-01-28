package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.exceptions.DefectDojoLoopException;
import io.securecodebox.persistence.defectdojo.models.DefectDojoModel;
import io.securecodebox.persistence.defectdojo.models.DefectDojoResponse;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

abstract public class GenericDefectDojoService<T extends DefectDojoModel> {
  protected String defectDojoUrl;
  protected String defectDojoApiKey;

  protected ObjectMapper objectMapper;
  protected ObjectMapper searchStringMapper;

  public GenericDefectDojoService(DefectDojoConfig config){
    this.defectDojoUrl = config.getUrl();
    this.defectDojoApiKey = config.getApiKey();

    this.objectMapper = new ObjectMapper();
    this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    this.searchStringMapper = new ObjectMapper();
    this.searchStringMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    this.searchStringMapper.setSerializationInclusion(JsonInclude.Include.NON_DEFAULT);
  }



  protected long DEFECT_DOJO_OBJET_LIMIT = 100L;

  /**
   * @return The DefectDojo Authentication Header
   */
  private HttpHeaders getDefectDojoAuthorizationHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Token " + defectDojoApiKey);
    return headers;
  }

  protected abstract String getUrlPath();

  protected abstract Class<T> getModelClass();

  protected abstract DefectDojoResponse<T> deserializeList(String response) throws JsonProcessingException;

  public T get(long id) {
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<String> payload = new HttpEntity<>(getDefectDojoAuthorizationHeaders());

    ResponseEntity<T> response = restTemplate.exchange(
      defectDojoUrl + "/api/v2/" + this.getUrlPath() + "/" + id,
      HttpMethod.GET,
      payload,
      getModelClass()
    );

    return response.getBody();
  }

  protected DefectDojoResponse<T> internalSearch(Map<String, Object> queryParams, long limit, long offset) throws JsonProcessingException, URISyntaxException {
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<String> payload = new HttpEntity<>(getDefectDojoAuthorizationHeaders());

    var mutableQueryParams = new HashMap<String, Object>(queryParams);

    mutableQueryParams.put("limit", String.valueOf(limit));
    mutableQueryParams.put("offset", String.valueOf(offset));

    var multiValueMap = new LinkedMultiValueMap<String, String>();
    for (var entry : mutableQueryParams.entrySet()) {
      multiValueMap.set(entry.getKey(), String.valueOf(entry.getValue()));
    }

    var url = new URI(defectDojoUrl + "/api/v2/" + this.getUrlPath() + "/");
    var uriBuilder = UriComponentsBuilder.fromUri(url).queryParams(multiValueMap);

    ResponseEntity<String> responseString = restTemplate.exchange(
      uriBuilder.build(mutableQueryParams),
      HttpMethod.GET,
      payload,
      String.class
    );

    return deserializeList(responseString.getBody());
  }

  public List<T> search(Map<String, Object> queryParams) throws URISyntaxException, JsonProcessingException {
    List<T> objects = new LinkedList<>();

    boolean hasNext = false;
    long page = 0;
    do {
      var response = internalSearch(queryParams, DEFECT_DOJO_OBJET_LIMIT, DEFECT_DOJO_OBJET_LIMIT * page++);
      objects.addAll(response.getResults());

      hasNext = response.getNext() != null;
      if (page > 100) {
        throw new DefectDojoLoopException("Found too many response object. Quitting after " + page + " paginated API pages of " + DEFECT_DOJO_OBJET_LIMIT + " each.");
      }
    } while (hasNext);

    return objects;
  }

  public List<T> search() throws URISyntaxException, JsonProcessingException {
    return search(new LinkedHashMap<>());
  }

  @SuppressWarnings("unchecked")
  public Optional<T> searchUnique(T searchObject) throws URISyntaxException, JsonProcessingException {
    Map<String, Object> queryParams = searchStringMapper.convertValue(searchObject, Map.class);

    var objects = search(queryParams);

    return objects.stream()
      .filter((object) -> object != null && object.equalsQueryString(queryParams))
      .findFirst();
  }

  public Optional<T> searchUnique(Map<String, Object> queryParams) throws URISyntaxException, JsonProcessingException {
    var objects = search(queryParams);

    return objects.stream()
      .filter((object) -> object.equalsQueryString(queryParams))
      .findFirst();
  }

  public T create(T object) {
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<T> payload = new HttpEntity<T>(object, getDefectDojoAuthorizationHeaders());

    ResponseEntity<T> response = restTemplate.exchange(defectDojoUrl + "/api/v2/" + getUrlPath() + "/", HttpMethod.POST, payload, getModelClass());
    return response.getBody();
  }

  public void delete(long id) {
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<String> payload = new HttpEntity<>(getDefectDojoAuthorizationHeaders());

    restTemplate.exchange(defectDojoUrl + "/api/v2/" + getUrlPath() + "/" + id + "/", HttpMethod.DELETE, payload, String.class);
  }

  public T update(T object, long objectId) {
    RestTemplate restTemplate = new RestTemplate();
    HttpEntity<T> payload = new HttpEntity<T>(object, getDefectDojoAuthorizationHeaders());

    ResponseEntity<T> response = restTemplate.exchange(defectDojoUrl + "/api/v2/" + getUrlPath() + "/" + objectId + "/", HttpMethod.PUT, payload, getModelClass());
    return response.getBody();
  }
}
