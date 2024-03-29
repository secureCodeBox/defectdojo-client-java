// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.model.Endpoint;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link EndpointService}
 */
final class EndpointServiceTest extends WireMockBaseTestCase {
  private static final String RESPONSE_LIST_FIXTURE_JSON = "EndpointService_response_list_fixture.json";
  private final EndpointService sut = new EndpointService(conf());
  private final Endpoint[] expectedFromSearch = new Endpoint[]{
    Endpoint.builder()
      .id(956)
      .protocol("tcp")
      .host("10.0.0.1")
      .port(80)
      .product(320)
      .build(),
    Endpoint.builder()
      .id(957)
      .protocol("tcp")
      .host("10.0.0.1")
      .port(443)
      .product(320)
      .build(),
    Endpoint.builder()
      .id(961)
      .protocol("tcp")
      .host("10.0.0.2")
      .port(80)
      .product(323)
      .build(),
    Endpoint.builder()
      .id(962)
      .protocol("tcp")
      .host("10.0.0.2")
      .port(443)
      .product(323)
      .build(),
    Endpoint.builder()
      .id(893)
      .protocol("tcp")
      .host("10.0.0.3")
      .port(443)
      .product(296)
      .build()};

  @Test
  void search() throws URISyntaxException, IOException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/endpoints/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(5)),
      () -> assertThat(result, containsInAnyOrder(expectedFromSearch))
    );
  }

  @Test
  void search_withQueryParams() throws URISyntaxException, IOException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/endpoints/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .withQueryParam("foo", equalTo("42"))
      .withQueryParam("bar", equalTo("23"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));
    final var params = new HashMap<String, Object>();
    params.put("foo", 42);
    params.put("bar", 23);

    final var result = sut.search(params);

    assertAll(
      () -> assertThat(result, hasSize(5)),
      () -> assertThat(result, containsInAnyOrder(expectedFromSearch))
    );
  }

  @Test
  void get_byId() {
    final var response = """
      {
        "id": 42,
        "tags": [],
        "protocol": "tcp",
        "userinfo": null,
        "host": "www.owasp.org",
        "port": 443,
        "path": null,
        "query": null,
        "fragment": null,
        "product": 285,
        "endpoint_params": [],
        "findings": [
          34706,
          34684,
          34679,
          34677
        ],
        "prefetch": {}
      }
      """;
    stubFor(get(urlPathEqualTo("/api/v2/endpoints/42"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));
    final var expected = Endpoint.builder()
      .id(42)
      .protocol("tcp")
      .host("www.owasp.org")
      .port(443)
      .product(285)
      .build();

    final var result = sut.get(42L);

    assertThat(result, is(expected));
  }


  @Test
  void searchUnique_withSearchObjectWhichReturnsEmptyResult() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/endpoints/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .withQueryParam("id", equalTo("42"))
      .withQueryParam("product", equalTo("285"))
      // Defaults from model:
      .withQueryParam("port", equalTo("0"))
      .withQueryParam("mitigated", equalTo("false"))
      .willReturn(ok()
        .withHeaders(responseHeaders(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE.length()))
        .withBody(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE)
      ));
    final var searchObject = Endpoint.builder()
      .id(42)
      .product(285)
      .build();

    final var result = sut.searchUnique(searchObject);

    assertThat(result.isEmpty(), is(true));
  }

  @Test
  void searchUnique_withQueryParamsWhichReturnsEmptyResult() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/endpoints/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .withQueryParam("foo", equalTo("42"))
      .withQueryParam("bar", equalTo("23"))
      .willReturn(ok()
        .withHeaders(responseHeaders(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE.length()))
        .withBody(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE)
      ));
    final var queryParams = new HashMap<String, Object>();
    queryParams.put("foo", 42);
    queryParams.put("bar", 23);

    final var result = sut.searchUnique(queryParams);

    assertThat(result.isEmpty(), is(true));
  }

  @Test
  void create() {
    final var json = """
      {
        "id": 42,
        "protocol": "tcp",
        "host": "www.owasp.org",
        "port":443,
        "product": 285,
        "mitigated": false
      }
      """;
    stubFor(post(urlPathEqualTo("/api/v2/endpoints/"))
      .withRequestBody(equalToJson(json))
      .willReturn(created()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json) // Typically the entity with new assigned id is returned, but we ignore this here.
      ));
    final var toCreate = Endpoint.builder()
      .id(42)
      .protocol("tcp")
      .host("www.owasp.org")
      .port(443)
      .product(285)
      .build();

    final var result = sut.create(toCreate);

    assertThat(result, is(toCreate));
  }

  @Test
  void delete_byId() {
    stubFor(delete(urlPathEqualTo("/api/v2/endpoints/42/"))
      .willReturn(ok()));

    sut.delete(42L);

    verify(deleteRequestedFor(urlPathEqualTo("/api/v2/endpoints/42/")));
  }

  @Test
  void update() {
    final var json = """
      {
        "id": 42,
        "protocol": "tcp",
        "host": "www.owasp.org",
        "port": 443,
        "product":285,
        "mitigated": false
      }
      """;
    stubFor(put(urlPathEqualTo("/api/v2/endpoints/42/"))
      .withRequestBody(equalToJson(json))
      .willReturn(ok()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json)
      ));

    final var toUpdate = Endpoint.builder()
      .id(42)
      .protocol("tcp")
      .host("www.owasp.org")
      .port(443)
      .product(285)
      .build();

    final var result = sut.update(toUpdate, 42L);

    assertThat(result, is(toUpdate));
  }
}
