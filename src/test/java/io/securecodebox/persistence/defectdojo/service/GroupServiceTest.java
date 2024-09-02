// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.model.Group;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link GroupService}
 */
final class GroupServiceTest extends WireMockBaseTestCase {
  private static final String RESPONSE_LIST_FIXTURE_JSON = "GroupService_response_list_fixture.json";
  private final GroupService sut = new GroupService(conf());
  private final Group[] expectedFromSearch = {Group.builder()
    .id(1L)
    .name("foo")
    .socialProvider("GitHub")
    .users(List.of(4L))
    .build(),
    Group.builder()
      .id(2L)
      .name("bar")
      .socialProvider("GitHub")
      .users(List.of(1L, 2L, 3L))
      .build(),
    Group.builder()
      .id(3L)
      .name("snafu")
      .socialProvider("GitHub")
      .users(List.of(4L, 5L))
      .build()};

  @Test
  void search() throws URISyntaxException, IOException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/dojo_groups/"))
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
    stubFor(get(urlPathEqualTo("/api/v2/dojo_groups/"))
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
        "id": 9,
        "configuration_permissions": [],
        "name": "team-orange",
        "description": null,
        "social_provider": "GitHub",
        "users": [
          4,
          5,
          6
        ],
        "prefetch": {}
      }
      """;
    stubFor(get(urlPathEqualTo("/api/v2/dojo_groups/9"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));
    final var expected = Group.builder()
      .id(9L)
      .name("team-orange")
      .socialProvider("GitHub")
      .users(List.of(4L, 5L, 6L))
      .build();

    final var result = sut.get(9);

    assertThat(result, is(expected));
  }


  @Test
  void searchUnique_withSearchObject() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/dojo_groups/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      .withQueryParam("name", equalTo("foo"))
      .willReturn(ok()
        .withHeaders(responseHeaders(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE.length()))
        .withBody(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE)
      ));

    final var searchObject = Group.builder()
      .name("foo")
      .build();

    final var result = sut.searchUnique(searchObject);

    assertThat(result.isEmpty(), is(true));
  }

  @Test
  void searchUnique_withQueryParams() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/dojo_groups/"))
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
        "description" : "bar",
        "users" : [ 1, 2, 3 ],
        "social_provider" : "GitHub"
      }
      """;
    stubFor(post(urlPathEqualTo("/api/v2/dojo_groups/"))
      .withRequestBody(equalToJson(json))
      .willReturn(created()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json) // Typically the entity with new assigned id is returned, but we ignore this here.
      ));
    final var toCreate = Group.builder()
      .id(42L)
      .name("foo")
      .description("bar")
      .socialProvider("GitHub")
      .users(List.of(1L, 2L, 3L))
      .build();

    final var result = sut.create(toCreate);

    assertThat(result, is(toCreate));
  }

  @Test
  void delete_byId() {
    stubFor(delete(urlPathEqualTo("/api/v2/dojo_groups/42/"))
      .willReturn(ok()));

    sut.delete(42L);

    verify(deleteRequestedFor(urlPathEqualTo("/api/v2/dojo_groups/42/")));
  }

  @Test
  void update() {
    final var json = """
      {
        "id" : 42,
        "name" : "foo",
        "description" : "bar",
        "users" : [ 1, 2, 3 ],
        "social_provider" : "GitHub"
      }
      """;
    stubFor(put(urlPathEqualTo("/api/v2/dojo_groups/42/"))
      .withRequestBody(equalToJson(json))
      .willReturn(ok()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json)
      ));

    final var toUpdate = Group.builder()
      .id(42L)
      .name("foo")
      .description("bar")
      .socialProvider("GitHub")
      .users(List.of(1L, 2L, 3L))
      .build();

    final var result = sut.update(toUpdate, 42L);

    assertThat(result, is(toUpdate));
  }
}
