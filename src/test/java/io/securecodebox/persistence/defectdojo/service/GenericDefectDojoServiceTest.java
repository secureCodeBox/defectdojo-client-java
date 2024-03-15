package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.model.Model;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import lombok.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link GenericDefectDojoService}
 */
final class GenericDefectDojoServiceTest extends WireMockBaseTestCase {
  private static final class TestModel implements Model {
    @JsonProperty
    private long id;

    @JsonProperty
    private String name = "";

    @Override
    public boolean equalsQueryString(@NonNull Map<String, Object> queryParams) {
      // Stub this to false because we do not test this method here.
      return false;
    }
  }

  private final GenericDefectDojoService<TestModel> sut = new GenericDefectDojoService<>(conf()) {

    @Override
    protected String getUrlPath() {
      return "snafu";
    }

    @Override
    protected Class<TestModel> getModelClass() {
      return TestModel.class;
    }

    @Override
    protected PaginatedResult<TestModel> deserializeList(@NonNull String response) {
      try {
        return this.objectMapper.readValue(response, new TypeReference<>() {
        });
      } catch (JsonProcessingException e) {
        throw new PersistenceException("Can't process JSON response!", e);
      }
    }
  };

  private static final String JSON_SINGLE_OBJECT = """
      {"id": 42, "name": "foo"}
    """;
  private static final String JSON_MULTIPLE_OBJECT = """
      {
        "count": 1,
        "next": null,
        "previous": null,
        "results": [
          {"id": 42, "name": "foo"}
        ],
        "prefetch": {}
      }
    """;

  @Test
  void createBaseUrl() {
    assertThat(sut.createBaseUrl(), is(URI.create("http://localhost:8888/api/v2/snafu/")));
  }

  @Test
  void get_containsAuthHeaderInRequest() {
    stubFor(get(urlPathEqualTo("/api/v2/snafu/42"))
      .willReturn(ok()
        .withHeaders(responseHeaders(JSON_SINGLE_OBJECT.length()))
        .withBody(JSON_SINGLE_OBJECT)));

    sut.get(42L);

    verify(getRequestedFor(urlPathEqualTo("/api/v2/snafu/42"))
      .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token not-required-for-tests"))
    );
  }

  @Test
  void search_containsAuthHeaderInRequest() {
    stubFor(get(urlPathEqualTo("/api/v2/snafu/"))
      .willReturn(ok()
        .withHeaders(responseHeaders(JSON_MULTIPLE_OBJECT.length()))
        .withBody(JSON_MULTIPLE_OBJECT)));

    sut.search();

    verify(getRequestedFor(urlPathEqualTo("/api/v2/snafu/"))
      .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token not-required-for-tests"))
    );
  }

  @Test
  void searchWithQueryParams_containsAuthHeaderInRequest() {
    stubFor(get(urlPathEqualTo("/api/v2/snafu/"))
      .willReturn(ok()
        .withHeaders(responseHeaders(JSON_MULTIPLE_OBJECT.length()))
        .withBody(JSON_MULTIPLE_OBJECT)));

    sut.search(Collections.emptyMap());

    verify(getRequestedFor(urlPathEqualTo("/api/v2/snafu/"))
      .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token not-required-for-tests"))
    );
  }

  @Test
  void searchUniqueWithObject_containsAuthHeaderInRequest() {
    stubFor(get(urlPathEqualTo("/api/v2/snafu/"))
      .willReturn(ok()
        .withHeaders(responseHeaders(JSON_MULTIPLE_OBJECT.length()))
        .withBody(JSON_MULTIPLE_OBJECT)));

    sut.searchUnique(new TestModel());

    verify(getRequestedFor(urlPathEqualTo("/api/v2/snafu/"))
      .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token not-required-for-tests"))
    );
  }

  @Test
  void searchUniqueWithQueryParams_containsAuthHeaderInRequest() {
    stubFor(get(urlPathEqualTo("/api/v2/snafu/"))
      .willReturn(ok()
        .withHeaders(responseHeaders(JSON_MULTIPLE_OBJECT.length()))
        .withBody(JSON_MULTIPLE_OBJECT)));

    sut.searchUnique(Collections.emptyMap());

    verify(getRequestedFor(urlPathEqualTo("/api/v2/snafu/"))
      .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token not-required-for-tests"))
    );
  }

  @Test
  void create_containsAuthHeaderInRequest() {
    stubFor(post(urlPathEqualTo("/api/v2/snafu/"))
      .willReturn(created()
        .withHeaders(responseHeaders(JSON_SINGLE_OBJECT.length()))
        .withBody(JSON_SINGLE_OBJECT)));

    sut.create(new TestModel());

    verify(postRequestedFor(urlPathEqualTo("/api/v2/snafu/"))
      .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token not-required-for-tests"))
    );
  }

  @Test
  void delete_containsAuthHeaderInRequest() {
    stubFor(delete(urlPathEqualTo("/api/v2/snafu/42/"))
      .willReturn(ok()));

    sut.delete(42);

    verify(deleteRequestedFor(urlPathEqualTo("/api/v2/snafu/42/"))
      .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token not-required-for-tests"))
    );
  }

  @Test
  void update_containsAuthHeaderInRequest() {
    stubFor(put(urlPathEqualTo("/api/v2/snafu/42/"))
      .willReturn(ok()
        .withHeaders(responseHeaders(JSON_SINGLE_OBJECT.length()))
        .withBody(JSON_SINGLE_OBJECT)));

    sut.update(new TestModel(), 42);

    verify(putRequestedFor(urlPathEqualTo("/api/v2/snafu/42/"))
      .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token not-required-for-tests"))
    );
  }
}
