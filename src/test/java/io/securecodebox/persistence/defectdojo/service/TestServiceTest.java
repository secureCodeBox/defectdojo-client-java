
// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
//
package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.model.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link TestService}
 */
final class TestServiceTest extends WireMockBaseTestCase {
  private static final String RESPONSE_LIST_FIXTURE_JSON = "TestService_response_list_fixture.json";
  private final TestService sut = new TestService(conf());
  private final Test[] expectedFromSearch = {Test.builder()
    .id(1L)
    .title("nuclei-owasp.org-1709280838-nmap-hostscan-6ckh4-nmap-su466fl")
    .description("# Nuclei Scan\nStarted: 01.03.2024 10:04:54\nEnded: 01.03.2024 15:06:30\nScanType: nuclei\nParameters: [-disable-update-check,-no-interactsh,-u,api.tokengate.dlt.owasp.org:443]")
    .targetStart("2024-03-01T10:04:54Z")
    .targetEnd("2024-03-01T15:06:29Z")
    .testType(67L)
    .lead(3L)
    .percentComplete(100L)
    .engagement(1L)
    .environment(1L)
    .build(),
    Test.builder()
      .id(2L)
      .title("nuclei-owasp.org-1709280838-nmap-hostscan-7xd2c-nuclei-5gzps")
      .description("# Nuclei Scan\nStarted: 01.03.2024 08:47:33\nEnded: 01.03.2024 15:06:34\nScanType: nuclei\nParameters: [-disable-update-check,-no-interactsh,-u,api.tokengate-dev.dlt.owasp.org]")
      .targetStart("2024-03-01T08:47:33Z")
      .targetEnd("2024-03-01T15:06:34Z")
      .testType(42L)
      .lead(23L)
      .percentComplete(43L)
      .engagement(2L)
      .environment(3L)
      .build(),
    Test.builder()
      .id(3L)
      .title("nuclei-owasp.org-1709280838-nmap-hostscan-6ckh4-nmap-sub6l7l")
      .description("# Nuclei Scan\nStarted: 01.03.2024 10:04:54\nEnded: 01.03.2024 15:06:35\nScanType: nuclei\nParameters: [-disable-update-check,-no-interactsh,-u,api.tokengate.dlt.owasp.org]")
      .targetStart("2024-03-01T10:04:54Z")
      .targetEnd("2024-03-01T15:06:35Z")
      .testType(67L)
      .lead(3L)
      .percentComplete(100L)
      .engagement(1L)
      .environment(1L)
      .build()};

  @org.junit.jupiter.api.Test
  void search() throws URISyntaxException, IOException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/tests/"))
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

  @org.junit.jupiter.api.Test
  void search_withQueryParams() throws IOException, URISyntaxException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/tests/"))
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

  @org.junit.jupiter.api.Test
  void get_byId() {
    final var response = """
      {
        "id": 200,
        "tags": [],
        "test_type_name": "Nmap Scan",
        "finding_groups": [],
        "scan_type": null,
        "title": "nmap-owasp.org-1709367238-nmap-hostscan-tlswv",
        "description": "# Nmap Scan...",
        "target_start": "2024-03-02T08:27:58Z",
        "target_end": "2024-03-02T08:45:05Z",
        "estimated_time": null,
        "actual_time": null,
        "percent_complete": 100,
        "updated": "2024-03-02T08:45:05.527633Z",
        "created": "2024-03-02T08:45:05.505448Z",
        "version": null,
        "build_id": null,
        "commit_hash": null,
        "branch_tag": null,
        "engagement": 64,
        "lead": 3,
        "test_type": 113,
        "environment": 1,
        "api_scan_configuration": null,
        "notes": [],
        "files": [],
        "prefetch": {}
        }
      """;
    stubFor(get(urlPathEqualTo("/api/v2/tests/200"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));
    final var expected = Test.builder()
      .id(200L)
      .title("nmap-owasp.org-1709367238-nmap-hostscan-tlswv")
      .description("# Nmap Scan...")
      .targetStart("2024-03-02T08:27:58Z")
      .targetEnd("2024-03-02T08:45:05Z")
      .testType(113L)
      .lead(3L)
      .percentComplete(100L)
      .engagement(64L)
      .build();

    final var result = sut.get(200);

    assertThat(result, is(expected));
  }


  @org.junit.jupiter.api.Test
  void searchUnique_withSearchObject() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/tests/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .withQueryParam("title", equalTo("foo"))
      // Defaults from model:
      .withQueryParam("environment", equalTo("1"))
      .withQueryParam("tags", equalTo("[]"))
      .willReturn(ok()
        .withHeaders(responseHeaders(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE.length()))
        .withBody(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE)
      ));

    final var searchObject = Test.builder()
      .title("foo")
      .build();

    final var result = sut.searchUnique(searchObject);

    assertThat(result.isEmpty(), is(true));
  }

  @org.junit.jupiter.api.Test
  void searchUnique_withQueryParams() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/tests/"))
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

  @org.junit.jupiter.api.Test
  void create() {
    final var json = """
      {
        "id" : 42,
        "title" : "foo",
        "description" : "bar",
        "tags" : [ ],
        "engagement" : 23,
        "environment" : 1,
        "target_start" : "start",
        "target_end" : "end",
        "test_type" : 5,
        "percent_complete" : 100
      }
      """;
    stubFor(post(urlPathEqualTo("/api/v2/tests/"))
      .withRequestBody(equalToJson(json))
      .willReturn(created()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json) // Typically the entity with new assigned id is returned, but we ignore this here.
      ));
    final var toCreate = Test.builder()
      .id(42L)
      .title("foo")
      .description("bar")
      .engagement(23L)
      .targetStart("start")
      .targetEnd("end")
      .percentComplete(100L)
      .testType(5L)
      .build();

    final var result = sut.create(toCreate);

    assertThat(result, is(toCreate));
  }

  @org.junit.jupiter.api.Test
  void delete_byId() {
    stubFor(delete(urlPathEqualTo("/api/v2/tests/42/"))
      .willReturn(ok()));

    sut.delete(42L);

    verify(deleteRequestedFor(urlPathEqualTo("/api/v2/tests/42/")));
  }

  @org.junit.jupiter.api.Test
  void update() {
    final var json = """
      {
        "id" : 42,
        "title" : "foo",
        "description" : "bar",
        "tags" : [ ],
        "engagement" : 23,
        "environment" : 1,
        "target_start" : "start",
        "target_end" : "end",
        "test_type" : 5,
        "percent_complete" : 100
      }
      """;
    stubFor(put(urlPathEqualTo("/api/v2/tests/42/"))
      .withRequestBody(equalToJson(json))
      .willReturn(ok()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json)
      ));

    final var toUpdate = Test.builder()
      .id(42L)
      .title("foo")
      .description("bar")
      .engagement(23L)
      .targetStart("start")
      .targetEnd("end")
      .percentComplete(100L)
      .testType(5L)
      .build();

    final var result = sut.update(toUpdate, 42L);

    assertThat(result, is(toUpdate));
  }
}
