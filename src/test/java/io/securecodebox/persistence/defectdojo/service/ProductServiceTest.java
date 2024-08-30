// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.model.Product;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link ProductService}
 */
final class ProductServiceTest extends WireMockBaseTestCase {
  private static final String RESPONSE_LIST_FIXTURE_JSON = "ProductService_response_list_fixture.json";
  private final ProductService sut = new ProductService(conf());
  private final Product[] expectedFromSearch = {Product.builder()
    .id(419L)
    .name("10.0.0.1")
    .description("Product was automatically created by the secureCodeBox DefectDojo integration")
    .tags(List.of(
      "attack-surface/internal",
      "office/munich",
      "org/owasp",
      "vlan/dev"
    ))
    .productType(2L)
    .findingsCount(12L)
    .enableFullRiskAcceptance(true)
    .authorizationGroups(Collections.emptyList())
    .enableSimpleRiskAcceptance(false)
    .build(),
    Product.builder()
      .id(312L)
      .name("10.0.0.2")
      .description("Product was automatically created by the secureCodeBox DefectDojo integration")
      .tags(List.of(
        "attack-surface/internal",
        "office/hamburg",
        "org/owasp",
        "vlan/dev"
      ))
      .productType(1L)
      .findingsCount(16L)
      .enableFullRiskAcceptance(false)
      .authorizationGroups(List.of(1L, 2L, 3L))
      .enableSimpleRiskAcceptance(false)
      .build(),
    Product.builder()
      .id(297L)
      .name("10.0.0.3")
      .description("Product was automatically created by the secureCodeBox DefectDojo integration")
      .tags(List.of(
        "attack-surface/external",
        "office/munich",
        "org/owasp"
      ))
      .productType(2L)
      .findingsCount(16L)
      .enableSimpleRiskAcceptance(true)
      .authorizationGroups(Collections.emptyList())
      .enableFullRiskAcceptance(false)
      .build()};

  @Test
  void search() throws URISyntaxException, IOException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/products/"))
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
    stubFor(get(urlPathEqualTo("/api/v2/products/"))
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
        "id": 320,
        "findings_count": 2,
        "findings_list": [
          18427,
          18426
        ],
        "tags": [
          "attack-surface/internal",
          "org/owasp"
        ],
        "product_meta": [],
        "name": "10.0.0.1",
        "description": "Product was automatically created by the secureCodeBox DefectDojo integration",
        "created": "2024-03-04T17:10:58.746715Z",
        "prod_numeric_grade": null,
        "business_criticality": null,
        "platform": null,
        "lifecycle": null,
        "origin": null,
        "user_records": null,
        "revenue": null,
        "external_audience": false,
        "internet_accessible": false,
        "enable_product_tag_inheritance": false,
        "enable_simple_risk_acceptance": false,
        "enable_full_risk_acceptance": true,
        "disable_sla_breach_notifications": false,
        "product_manager": null,
        "technical_contact": null,
        "team_manager": null,
        "prod_type": 2,
        "sla_configuration": 1,
        "members": [],
        "authorization_groups": [],
        "regulations": [],
        "prefetch": {}
      }
      """;
    stubFor(get(urlPathEqualTo("/api/v2/products/320"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));
    final var expected = Product.builder()
      .id(320L)
      .name("10.0.0.1")
      .description("Product was automatically created by the secureCodeBox DefectDojo integration")
      .tags(List.of(
        "attack-surface/internal",
        "org/owasp"
      ))
      .findingsCount(2L)
      .productType(2L)
      .enableFullRiskAcceptance(true)
      .enableSimpleRiskAcceptance(false)
      .build();

    final var result = sut.get(320);

    assertThat(result, is(expected));
  }

  @Test
  void searchUnique_withSearchObject() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/products/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .withQueryParam("name", equalTo("foo"))
      .willReturn(ok()
        .withHeaders(responseHeaders(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE.length()))
        .withBody(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE)
      ));

    final var searchObject = Product.builder()
      .name("foo")
      .build();

    final var result = sut.searchUnique(searchObject);

    assertThat(result.isEmpty(), is(true));
  }

  @Test
  void searchUnique_withQueryParams() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/products/"))
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
        "tags" : [ "foo", "bar", "baz" ],
        "description" : "bar",
        "prod_type" : 23,
        "authorization_groups" : [ ]
      }
      """;
    stubFor(post(urlPathEqualTo("/api/v2/products/"))
      .withRequestBody(equalToJson(json))
      .willReturn(created()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json) // Typically the entity with new assigned id is returned, but we ignore this here.
      ));
    final var toCreate = Product.builder()
      .id(42L)
      .name("foo")
      .description("bar")
      .productType(23L)
      .tags(List.of("foo", "bar", "baz"))
      .build();

    final var result = sut.create(toCreate);

    assertThat(result, is(toCreate));
  }

  @Test
  void delete_byId() {
    stubFor(delete(urlPathEqualTo("/api/v2/products/42/"))
      .willReturn(ok()));

    sut.delete(42L);

    verify(deleteRequestedFor(urlPathEqualTo("/api/v2/products/42/")));
  }

  @Test
  void update() {
    final var json = """
      {
        "id" : 42,
        "name" : "foo",
        "tags" : [ "foo", "bar", "baz" ],
        "description" : "bar",
        "prod_type" : 23,
        "authorization_groups" : [ ]
      }
      """;
    stubFor(put(urlPathEqualTo("/api/v2/products/42/"))
      .withRequestBody(equalToJson(json))
      .willReturn(ok()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json)
      ));

    final var toUpdate = Product.builder()
      .id(42L)
      .name("foo")
      .description("bar")
      .productType(23L)
      .tags(List.of("foo", "bar", "baz"))
      .build();

    final var result = sut.update(toUpdate, 42L);

    assertThat(result, is(toUpdate));
  }
}
