// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.model.Finding;
import io.securecodebox.persistence.defectdojo.model.RiskAcceptance;
import lombok.Builder;
import lombok.NonNull;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link FindingService}
 */
final class FindingServiceTest extends WireMockBaseTestCase {
  private static final String RESPONSE_LIST_FIXTURE_JSON = "FindingService_response_list_fixture.json";
  private static final String RESPONSE_SINGLE_FIXTURE_JSON = "FindingService_response_single_fixture.json";
  private final FindingService sut = new FindingService(conf());
  private final Finding expectedFromSearch = Finding.builder()
    .id(42)
    .title("Open Port: 9929/TCP")
    .description("IP Address: 198.51.100.0 FQDN: scanme.nmap.org Port/Protocol: 9929/tcp")
    .foundBy(List.of(132L))
    .severity(Finding.Severity.INFORMATIONAL)
    .test(222)
    .mitigation("N/A")
    .impact("No impact provided")
    .verified(true)
    .active(true)
    .endpoints(List.of(875L))
    .createdAt(OffsetDateTime.parse("2021-07-21T12:43:36.549669Z"))
    .acceptedRisks(Collections.emptyList())
    .build();

  @Test
  void deserializeList_shouldResultExactlyOneResult() throws IOException {
    var result = sut.deserializeList(readFixtureFile(RESPONSE_LIST_FIXTURE_JSON));

    assertThat(result.getCount(), is(1));
  }

  @Test
  void search() throws URISyntaxException, IOException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/findings/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(1)),
      () -> assertThat(result, containsInAnyOrder(expectedFromSearch))
    );
  }

  @Test
  void search_withQueryParams() throws IOException, URISyntaxException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/findings/"))
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
      () -> assertThat(result, hasSize(1)),
      () -> assertThat(result, containsInAnyOrder(expectedFromSearch))
    );
  }

  @Test
  void get_byId() throws IOException {
    final var response = readFixtureFile(RESPONSE_SINGLE_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/findings/42"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));
    final var expected = Finding.builder()
      .id(42)
      .title("www.owasp.org")
      .description("Found subdomain www.owasp.org")
      .severity(Finding.Severity.INFORMATIONAL)
      .foundBy(List.of(128L))
      .test(17)
      .active(true)
      .endpoints(List.of(5L))
      .createdAt(OffsetDateTime.parse("2024-03-02T08:28:00.407414Z"))
      .build();

    final var result = sut.get(42L);

    assertThat(result, is(expected));
  }


  @Test
  void searchUnique_withSearchObject() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/findings/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .withQueryParam("title", equalTo("foo"))
      .withQueryParam("description", equalTo("snafu"))
      .withQueryParam("found_by", equalTo("[12, 42]"))
      .withQueryParam("severity", equalTo("High"))
      // Defaults from model:
      .withQueryParam("endpoints", equalTo("[]"))
      .withQueryParam("test", equalTo("0"))
      .withQueryParam("verified", equalTo("false"))
      .withQueryParam("active", equalTo("false"))
      .withQueryParam("duplicate", equalTo("false"))
      .withQueryParam("out_of_scope", equalTo("false"))
      .withQueryParam("risk_accepted", equalTo("false"))
      .withQueryParam("id", equalTo("0"))
      .withQueryParam("duplicate_finding", equalTo("0"))
      .withQueryParam("numerical_severity", equalTo("S1"))
      .withQueryParam("false_p", equalTo("false"))
      .willReturn(ok()
        .withHeaders(responseHeaders(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE.length()))
        .withBody(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE)
      ));

    final var searchObject = Finding.builder()
      .title("foo")
      .description("snafu")
      .foundBy(List.of(12L, 42L))
      .severity(Finding.Severity.HIGH)
      .build();

    final var result = sut.searchUnique(searchObject);

    assertThat(result.isEmpty(), is(true));
  }

  @Test
  void searchUnique_withQueryParams() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/findings/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .withQueryParam("foo", equalTo("42"))
      .withQueryParam("bar", equalTo("23"))
      .willReturn(ok()
        .withHeaders(responseHeaders(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE.length()))
        .withBody(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE
        )));

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
        "id" : 0,
        "title" : "foo",
        "description" : "bar",
        "severity" : "High",
        "test" : 0,
        "active" : false,
        "verified" : false,
        "duplicate" : false,
        "endpoints" : [ ],
        "found_by" : [ 1, 2, 3 ],
        "risk_accepted" : false,
        "out_of_scope" : false,
        "duplicate_finding" : 0,
        "false_p" : false,
        "accepted_risks" : [ ],
        "numerical_severity" : "S1"
      }
      """;
    stubFor(post(urlPathEqualTo("/api/v2/findings/"))
      .withRequestBody(equalToJson(json))
      .willReturn(created()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json) // Typically the entity with new assigned id is returned, but we ignore this here.
      ));
    final var toCreate = Finding.builder()
      .title("foo")
      .description("bar")
      .foundBy(List.of(1L, 2L, 3L))
      .severity(Finding.Severity.HIGH)
      .build();

    final var result = sut.create(toCreate);

    assertThat(result, is(toCreate));
  }

  @Test
  void delete_byId() {
    stubFor(delete(urlPathEqualTo("/api/v2/findings/42/"))
      .willReturn(ok()));

    sut.delete(42L);

    verify(deleteRequestedFor(urlPathEqualTo("/api/v2/findings/42/")));
  }

  @Test
  void update() {
    final var json = """
      {
        "id" : 0,
        "title" : "foo",
        "description" : "bar",
        "severity" : "High",
        "test" : 0,
        "active" : false,
        "verified" : false,
        "duplicate" : false,
        "endpoints" : [ ],
        "found_by" : [ 1, 2, 3 ],
        "risk_accepted" : false,
        "out_of_scope" : false,
        "duplicate_finding" : 0,
        "false_p" : false,
        "accepted_risks" : [ ],
        "numerical_severity" : "S1"
      }
      """;
    stubFor(put(urlPathEqualTo("/api/v2/findings/42/"))
      .withRequestBody(equalToJson(json))
      .willReturn(ok()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json)
      ));

    final var toUpdate = Finding.builder()
      .title("foo")
      .description("bar")
      .foundBy(List.of(1L, 2L, 3L))
      .severity(Finding.Severity.HIGH)
      .build();

    final var result = sut.update(toUpdate, 42L);

    assertThat(result, is(toUpdate));
  }
}
