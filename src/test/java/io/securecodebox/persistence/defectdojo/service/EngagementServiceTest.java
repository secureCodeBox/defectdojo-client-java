// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.model.Engagement;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link EngagementService}
 */
final class EngagementServiceTest extends WireMockBaseTestCase {
  private static final String RESPONSE_LIST_FIXTURE_JSON = "EngagementService_response_list_fixture.json";
  private final EngagementService sut = new EngagementService(conf());
  private final Engagement[] expectedFromSearch = {Engagement.builder()
    .id(806L)
    .branch("")
    .name("nmap-vienna-client-1709886900")
    .description("")
    .version("")
    .targetStart("2024-03-08")
    .targetEnd("2024-03-08")
    .status(Engagement.Status.IN_PROGRESS)
    .engagementType("CI/CD")
    .lead(3L)
    .product(162L)
    .orchestrationEngine(1L)
    .tags(Collections.emptyList())
    .build(),
    Engagement.builder()
      .id(807L)
      .branch("")
      .name("nmap-stuttgart-client-1709886900")
      .description("")
      .version("")
      .targetStart("2024-03-08")
      .targetEnd("2024-03-08")
      .status(Engagement.Status.IN_PROGRESS)
      .engagementType("CI/CD")
      .lead(3L)
      .product(139L)
      .orchestrationEngine(1L)
      .tags(Collections.emptyList())
      .build(),
    Engagement.builder()
      .id(808L)
      .branch("")
      .name("nmap-frankfurt-client-1709886900")
      .description("")
      .version("")
      .targetStart("2024-03-08")
      .targetEnd("2024-03-08")
      .status(Engagement.Status.IN_PROGRESS)
      .engagementType("CI/CD")
      .lead(3L)
      .product(140L)
      .orchestrationEngine(1L)
      .tags(Collections.emptyList())
      .build()};

  @Test
  void search() throws URISyntaxException, IOException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/engagements/"))
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
    stubFor(get(urlPathEqualTo("/api/v2/engagements/"))
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
        "tags": [],
        "name": "nmap-office-client-1710146100",
        "description": "",
        "version": "",
        "first_contacted": null,
        "target_start": "2024-03-11",
        "target_end": "2024-03-11",
        "reason": null,
        "updated": "2024-03-11T08:35:42.722442Z",
        "created": "2024-03-11T08:35:42.169727Z",
        "active": true,
        "tracker": null,
        "test_strategy": null,
        "threat_model": false,
        "api_test": false,
        "pen_test": false,
        "check_list": false,
        "status": "In Progress",
        "progress": "threat_model",
        "tmodel_path": "none",
        "done_testing": false,
        "engagement_type": "CI/CD",
        "build_id": null,
        "commit_hash": null,
        "branch_tag": "",
        "source_code_management_uri": null,
        "deduplication_on_engagement": false,
        "lead": 3,
        "requester": null,
        "preset": null,
        "report_type": null,
        "product": 139,
        "build_server": null,
        "source_code_management_server": null,
        "orchestration_engine": 1,
        "notes": [],
        "files": [],
        "risk_acceptance": [],
        "prefetch": {}
      }
      """;
    stubFor(get(urlPathEqualTo("/api/v2/engagements/42"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));
    final var expected = Engagement.builder()
      .id(42L)
      .name("nmap-office-client-1710146100")
      .product(139L)
      .targetStart("2024-03-11")
      .targetEnd("2024-03-11")
      .lead(3L)
      .orchestrationEngine(1L)
      .build();

    final var result = sut.get(42L);

    assertThat(result, is(expected));
  }


  @Test
  void searchUnique_withSearchObject() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/engagements/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .withQueryParam("version", equalTo("foo"))
      .withQueryParam("name", equalTo("bar"))
      // Defaults from model:
      .withQueryParam("check_list", equalTo("false"))
      .withQueryParam("pen_test", equalTo("false"))
      .withQueryParam("threat_model", equalTo("false"))
      .withQueryParam("engagement_type", equalTo("CI/CD"))
      .withQueryParam("deduplication_on_engagement", equalTo("false"))
      .withQueryParam("api_test", equalTo("false"))
      .withQueryParam("status", equalTo("In Progress"))
      .willReturn(ok()
        .withHeaders(responseHeaders(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE.length()))
        .withBody(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE)
      ));
    final var searchObject = Engagement.builder()
      .version("foo")
      .name("bar")
      .build();

    final var result = sut.searchUnique(searchObject);

    assertThat(result.isEmpty(), is(true));
  }

  @Test
  void searchUnique_withQueryParams() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/engagements/"))
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
         "name" : "nmap-office-client-1710146100",
         "product" : 139,
         "lead" : 3,
         "status" : "In Progress",
         "tags" : [ ],
         "description" : "SNAFU",
         "version" : "",
         "branch_tag" : "",
         "target_start" : "2024-03-11",
         "target_end" : "2024-03-11",
         "engagement_type" : "CI/CD",
         "orchestration_engine" : 1,
         "deduplication_on_engagement" : false,
         "threat_model" : true,
         "api_test" : false,
         "pen_test" : false,
         "check_list" : false
      }
      """;
    stubFor(post(urlPathEqualTo("/api/v2/engagements/"))
      .withRequestBody(equalToJson(json))
      .willReturn(created()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json) // Typically the entity with new assigned id is returned, but we ignore this here.
      ));
    final var toCreate = Engagement.builder()
      .id(42L)
      .name("nmap-office-client-1710146100")
      .description("SNAFU")
      .targetStart("2024-03-11")
      .targetEnd("2024-03-11")
      .status(Engagement.Status.IN_PROGRESS)
      .threatModel(true)
      .engagementType("CI/CD")
      .lead(3L)
      .product(139L)
      .orchestrationEngine(1L)
      .build();

    final var result = sut.create(toCreate);

    assertThat(result, is(toCreate));
  }

  @Test
  void delete_byId() {
    stubFor(delete(urlPathEqualTo("/api/v2/engagements/42/"))
      .willReturn(ok()));

    sut.delete(42L);

    verify(deleteRequestedFor(urlPathEqualTo("/api/v2/engagements/42/")));
  }

  @Test
  void update() {
    final var json = """
      {
        "id" : 42,
        "name" : "nmap-office-client-1710146100",
        "product" : 139,
        "lead" : 3,
        "status" : "In Progress",
        "tags" : [ ],
        "description" : "SNAFU",
        "version" : "",
        "branch_tag" : "",
        "target_start" : "2024-03-11",
        "target_end" : "2024-03-11",
        "engagement_type" : "CI/CD",
        "orchestration_engine" : 1,
        "deduplication_on_engagement" : false,
        "threat_model" : true,
        "api_test" : false,
        "pen_test" : false,
        "check_list" : false
      }
      """;
    stubFor(put(urlPathEqualTo("/api/v2/engagements/42/"))
      .withRequestBody(equalToJson(json))
      .willReturn(ok()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json)
      ));

    final var toUpdate = Engagement.builder()
      .id(42L)
      .name("nmap-office-client-1710146100")
      .description("SNAFU")
      .targetStart("2024-03-11")
      .targetEnd("2024-03-11")
      .status(Engagement.Status.IN_PROGRESS)
      .threatModel(true)
      .engagementType("CI/CD")
      .lead(3L)
      .product(139L)
      .orchestrationEngine(1L)
      .build();

    final var result = sut.update(toUpdate, 42L);

    assertThat(result, is(toUpdate));
  }
}
