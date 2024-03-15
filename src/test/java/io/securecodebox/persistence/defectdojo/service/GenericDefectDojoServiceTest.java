package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import io.securecodebox.persistence.defectdojo.http.ProxyConfig;
import io.securecodebox.persistence.defectdojo.model.Model;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import lombok.NonNull;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Nested;
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
final class GenericDefectDojoServiceTest {

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

  private static final class TestableGenericDefectDojoService extends GenericDefectDojoService<TestModel> {
    private TestableGenericDefectDojoService(ClientConfig clientConfig) {
      super(clientConfig);
    }

    private TestableGenericDefectDojoService(@NonNull ClientConfig clientConfig, @NonNull ProxyConfig proxyConfig) {
      super(clientConfig, proxyConfig);
    }

    @Override
    protected String getUrlPath() {
      return "snafu";
    }

    @Override
    protected Class<TestModel> getModelClass() {
      return TestModel.class;
    }

    @Override
    @SneakyThrows
    protected PaginatedResult<TestModel> deserializeList(@NonNull String response) {
      return modelObjectMapper().readValue(response, new TypeReference<>() {
      });
    }
  }

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

  private final ClientConfig clientConfig = new ClientConfig("https://defectdojo.example.com:8080","api-key");
  private final TestableGenericDefectDojoService sut = new TestableGenericDefectDojoService(clientConfig);

  @Test
  void createBaseUrl() {
    assertThat(sut.createBaseUrl(), is(URI.create("https://defectdojo.example.com:8080/api/v2/snafu/")));
  }

  @Nested
  class AuthenticationHeaderWithoutProxyConfig extends WireMockBaseTestCase {
    private final TestableGenericDefectDojoService sut = new TestableGenericDefectDojoService(
      conf(), ProxyConfig.NULL
    );

    @Test
    void get_containsAuthHeaderInRequest() {
      stubFor(get(urlPathEqualTo("/api/v2/snafu/42"))
        .willReturn(ok()
          .withHeaders(responseHeaders(JSON_SINGLE_OBJECT.length()))
          .withBody(JSON_SINGLE_OBJECT)));

      sut.get(42L);

      verify(getRequestedFor(urlPathEqualTo("/api/v2/snafu/42"))
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withoutHeader(HttpHeaders.PROXY_AUTHORIZATION)
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
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withoutHeader(HttpHeaders.PROXY_AUTHORIZATION)
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
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withoutHeader(HttpHeaders.PROXY_AUTHORIZATION)
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
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withoutHeader(HttpHeaders.PROXY_AUTHORIZATION)
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
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withoutHeader(HttpHeaders.PROXY_AUTHORIZATION)
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
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withoutHeader(HttpHeaders.PROXY_AUTHORIZATION)
      );
    }

    @Test
    void delete_containsAuthHeaderInRequest() {
      stubFor(delete(urlPathEqualTo("/api/v2/snafu/42/"))
        .willReturn(ok()));

      sut.delete(42);

      verify(deleteRequestedFor(urlPathEqualTo("/api/v2/snafu/42/"))
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withoutHeader(HttpHeaders.PROXY_AUTHORIZATION)
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
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withoutHeader(HttpHeaders.PROXY_AUTHORIZATION)
      );
    }
  }

  @Nested
  class AuthenticationHeaderWithProxyConfig extends WireMockBaseTestCase {
    private final ProxyConfig proxyConfig = ProxyConfig.builder()
      .user("alf")
      .password("test1234")
      .host("proxy.owasp.org")
      .port(8080)
      .build();
    private final TestableGenericDefectDojoService sut = new TestableGenericDefectDojoService(
      conf(), proxyConfig
    );

    @Test
    void get_containsAuthHeaderInRequest() {
      stubFor(get(urlPathEqualTo("/api/v2/snafu/42"))
        .willReturn(ok()
          .withHeaders(responseHeaders(JSON_SINGLE_OBJECT.length()))
          .withBody(JSON_SINGLE_OBJECT)));

      sut.get(42L);

      verify(getRequestedFor(urlPathEqualTo("/api/v2/snafu/42"))
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withHeader(HttpHeaders.PROXY_AUTHORIZATION, equalTo("Basic YWxmOnRlc3QxMjM0"))
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
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withHeader(HttpHeaders.PROXY_AUTHORIZATION, equalTo("Basic YWxmOnRlc3QxMjM0"))
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
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withHeader(HttpHeaders.PROXY_AUTHORIZATION, equalTo("Basic YWxmOnRlc3QxMjM0"))
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
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withHeader(HttpHeaders.PROXY_AUTHORIZATION, equalTo("Basic YWxmOnRlc3QxMjM0"))
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
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withHeader(HttpHeaders.PROXY_AUTHORIZATION, equalTo("Basic YWxmOnRlc3QxMjM0"))
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
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withHeader(HttpHeaders.PROXY_AUTHORIZATION, equalTo("Basic YWxmOnRlc3QxMjM0"))
      );
    }

    @Test
    void delete_containsAuthHeaderInRequest() {
      stubFor(delete(urlPathEqualTo("/api/v2/snafu/42/"))
        .willReturn(ok()));

      sut.delete(42);

      verify(deleteRequestedFor(urlPathEqualTo("/api/v2/snafu/42/"))
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withHeader(HttpHeaders.PROXY_AUTHORIZATION, equalTo("Basic YWxmOnRlc3QxMjM0"))
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
        .withHeader(HttpHeaders.AUTHORIZATION, equalTo("Token " + API_KEY))
        .withHeader(HttpHeaders.PROXY_AUTHORIZATION, equalTo("Basic YWxmOnRlc3QxMjM0"))
      );
    }
  }
}
