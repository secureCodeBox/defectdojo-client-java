// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.model.TestType;
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
 * Tests for {@link TestTypeService}
 */
final class TestTypeServiceTest extends WireMockBaseTestCase {
  private static final String RESPONSE_LIST_FIXTURE_JSON = "TestTypeService_response_list_fixture.json";
  private final TestTypeService sut = new TestTypeService(conf());
  private final TestType[] expectedFromSearch = {TestType.builder()
    .id(99)
    .name("Acunetix360 Scan")
    .staticTool(false)
    .dynamicTool(false)
    .build(),
    TestType.builder()
      .id(19)
      .name("Acunetix Scan")
      .staticTool(false)
      .dynamicTool(false)
      .build(),
    TestType.builder()
      .id(125)
      .name("AnchoreCTL Policies Report")
      .staticTool(false)
      .dynamicTool(false)
      .build(),
    TestType.builder()
      .id(47)
      .name("AnchoreCTL Vuln Report")
      .staticTool(false)
      .dynamicTool(false)
      .build(),
    TestType.builder()
      .id(112)
      .name("Anchore Engine Scan")
      .staticTool(false)
      .dynamicTool(false)
      .build(),
    TestType.builder()
      .id(172)
      .name("Anchore Enterprise Policy Check")
      .staticTool(false)
      .dynamicTool(false)
      .build(),
    TestType.builder()
      .id(106)
      .name("Anchore Grype")
      .staticTool(true)
      .dynamicTool(false)
      .build(),
    TestType.builder()
      .id(1)
      .name("API Test")
      .staticTool(false)
      .dynamicTool(false)
      .build(),
    TestType.builder()
      .id(100)
      .name("AppSpider Scan")
      .staticTool(false)
      .dynamicTool(true)
      .build()};

  @Test
  void search() throws URISyntaxException, IOException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/test_types/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(9)),
      () -> assertThat(result, containsInAnyOrder(expectedFromSearch))
    );
  }

  @Test
  void search_withQueryParams() throws IOException, URISyntaxException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/test_types/"))
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
      () -> assertThat(result, hasSize(9)),
      () -> assertThat(result, containsInAnyOrder(expectedFromSearch))
    );
  }

  @Test
  void get_byId() {
    final var response = """
      {
        "id": 42,
        "name": "SNAFU Scan",
        "static_tool": true,
        "dynamic_tool": true,
        "active": true
      }
      """;
    stubFor(get(urlPathEqualTo("/api/v2/test_types/42"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));
    final var expected = TestType.builder()
      .id(42)
      .name("SNAFU Scan")
      .dynamicTool(true)
      .staticTool(true)
      .build();

    final var result = sut.get(42L);

    assertThat(result, is(expected));
  }


  @Test
  void searchUnique_withSearchObject() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/test_types/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .withQueryParam("name", equalTo("foo"))
      // Defaults from model:
      .withQueryParam("static_tool", equalTo("false"))
      .withQueryParam("dynamic_tool", equalTo("false"))
      .withQueryParam("id", equalTo("0"))
      .willReturn(ok()
        .withHeaders(responseHeaders(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE.length()))
        .withBody(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE)
      ));

    final var searchObject = TestType.builder()
      .name("foo")
      .build();

    final var result = sut.searchUnique(searchObject);

    assertThat(result.isEmpty(), is(true));
  }

  @Test
  void searchUnique_withQueryParams() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/test_types/"))
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
        "id" : 42,
        "name" : "foo",
        "static_tool" : true,
        "dynamic_tool" : true
      }
      """;
    stubFor(post(urlPathEqualTo("/api/v2/test_types/"))
      .withRequestBody(equalToJson(json))
      .willReturn(created()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json) // Typically the entity with new assigned id is returned, but we ignore this here.
      ));
    final var toCreate = TestType.builder()
      .id(42)
      .name("foo")
      .staticTool(true)
      .dynamicTool(true)
      .build();

    final var result = sut.create(toCreate);

    assertThat(result, is(toCreate));
  }

  @Test
  void delete_byId() {
    stubFor(delete(urlPathEqualTo("/api/v2/test_types/42/"))
      .willReturn(ok()));

    sut.delete(42L);

    verify(deleteRequestedFor(urlPathEqualTo("/api/v2/test_types/42/")));
  }

  @Test
  void update() {
    final var json = """
      {
        "id" : 42,
        "name" : "foo",
        "static_tool" : true,
        "dynamic_tool" : true
      }
      """;
    stubFor(put(urlPathEqualTo("/api/v2/test_types/42/"))
      .withRequestBody(equalToJson(json))
      .willReturn(ok()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json)
      ));

    final var toUpdate = TestType.builder()
      .id(42)
      .name("foo")
      .staticTool(true)
      .dynamicTool(true)
      .build();

    final var result = sut.update(toUpdate, 42L);

    assertThat(result, is(toUpdate));
  }
}
