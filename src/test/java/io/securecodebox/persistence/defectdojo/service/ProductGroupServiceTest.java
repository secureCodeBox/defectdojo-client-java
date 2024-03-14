// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.model.ProductGroup;
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
 * Tests for {@link ProductGroupService}
 */
final class ProductGroupServiceTest extends WireMockBaseTestCase {
  private static final String RESPONSE_LIST_FIXTURE_JSON = "ProductGroupService_response_list_fixture.json";
  private final ProductGroupService sut = new ProductGroupService(conf());
  private final ProductGroup[] expectedFromSearch = new ProductGroup[]{
    ProductGroup.builder()
      .id(1)
      .product(2)
      .group(3)
      .role(4)
      .build(),
    ProductGroup.builder()
      .id(5)
      .product(6)
      .group(7)
      .role(8)
      .build(),
    ProductGroup.builder()
      .id(9)
      .product(10)
      .group(11)
      .role(12)
      .build()
  };

  @Test
  void search() throws URISyntaxException, IOException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/product_groups/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(3)),
      () -> assertThat(result, containsInAnyOrder(expectedFromSearch))
    );
  }

  @Test
  void search_withQueryParams() throws IOException, URISyntaxException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/product_groups/"))
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
      () -> assertThat(result, hasSize(3)),
      () -> assertThat(result, containsInAnyOrder(expectedFromSearch))
    );
  }

  @Test
  void get_byId() {
    final var response = """
      {
        "id": 42,
        "product": 2,
        "group": 3,
        "role": 4
      }
      """;
    stubFor(get(urlPathEqualTo("/api/v2/product_groups/42"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));
    final var expected = ProductGroup.builder()
      .id(42)
      .product(2)
      .group(3)
      .role(4)
      .build();

    final var result = sut.get(42L);

    assertThat(result, is(expected));
  }


  @Test
  void searchUnique_withSearchObject() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/product_groups/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .withQueryParam("product", equalTo("42"))
      // Defaults from model:
      .withQueryParam("id", equalTo("0"))
      .withQueryParam("role", equalTo("0"))
      .withQueryParam("group", equalTo("0"))
      .willReturn(ok()
        .withHeaders(responseHeaders(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE.length()))
        .withBody(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE)
      ));

    final var searchObject = ProductGroup.builder()
      .product(42)
      .build();

    final var result = sut.searchUnique(searchObject);

    assertThat(result.isEmpty(), is(true));
  }

  @Test
  void searchUnique_withQueryParams() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/product_groups/"))
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
        "product": 285,
        "group": 23,
        "role": 47
      }
      """;
    stubFor(post(urlPathEqualTo("/api/v2/product_groups/"))
      .withRequestBody(equalToJson(json))
      .willReturn(created()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json) // Typically the entity with new assigned id is returned, but we ignore this here.
      ));
    final var toCreate = ProductGroup.builder()
      .id(42)
      .product(285)
      .group(23)
      .role(47)
      .build();

    final var result = sut.create(toCreate);

    assertThat(result, is(toCreate));
  }

  @Test
  void delete_byId() {
    stubFor(delete(urlPathEqualTo("/api/v2/product_groups/42/"))
      .willReturn(ok()));

    sut.delete(42L);

    verify(deleteRequestedFor(urlPathEqualTo("/api/v2/product_groups/42/")));
  }

  @Test
  void update() {
    final var json = """
      {
        "id": 42,
        "product": 285,
        "group": 23,
        "role": 47
      }
      """;
    stubFor(put(urlPathEqualTo("/api/v2/product_groups/42/"))
      .withRequestBody(equalToJson(json))
      .willReturn(ok()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json)
      ));

    final var toUpdate = ProductGroup.builder()
      .id(42)
      .product(285)
      .group(23)
      .role(47)
      .build();

    final var result = sut.update(toUpdate, 42L);

    assertThat(result, is(toUpdate));
  }
}
