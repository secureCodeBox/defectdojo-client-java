// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.model.User;
import io.securecodebox.persistence.defectdojo.model.UserProfile;
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
 * Tests for {@link UserProfileService}
 * <p>
 * This test is special because the defectdojo api does not return a list, but the generic code assumes every endpoint
 * returns a list.
 * </p>
 */
final class UserProfileServiceTest extends WireMockBaseTestCase {
  private static final String RESPONSE_LIST_FIXTURE_JSON = "UserProfileService_response_list_fixture.json";
  private final UserProfileService sut = new UserProfileService(conf());
  private final UserProfile expectedFromSearch = UserProfile.builder()
    .user(new User(0L, "username", "first_name", "last_name"))
    .build();

  @Test
  void search() throws URISyntaxException, IOException {
    final var response = readFixtureFile(RESPONSE_LIST_FIXTURE_JSON);
    stubFor(get(urlPathEqualTo("/api/v2/user_profile/"))
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
    stubFor(get(urlPathEqualTo("/api/v2/user_profile/"))
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
  void get_byId() {
    final var response = """
      {
        "user": {
          "id": 42,
          "username": "alf",
          "first_name": "Gordon",
          "last_name": "Shumway",
          "email": "gordon.shumway@owasp.org",
          "last_login": "2022-11-01T16:20:19.373Z",
          "is_active": true,
          "is_superuser": true,
          "configuration_permissions": [0]
        }
      }
      """;
    stubFor(get(urlPathEqualTo("/api/v2/user_profile/42"))
      .willReturn(ok()
        .withHeaders(responseHeaders(response.length()))
        .withBody(response)
      ));
    final var expected = UserProfile.builder()
      .user(User.builder()
        .id(42)
        .username("alf")
        .firstName("Gordon")
        .lastName("Shumway")
        .build())
      .build();

    final var result = sut.get(42L);

    assertThat(result, is(expected));
  }

  @Test
  void searchUnique_withSearchObject() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/user_profile/"))
      .withQueryParam("limit", equalTo("100"))
      .withQueryParam("offset", equalTo("0"))
      // Defaults from model:
      .willReturn(ok()
        .withHeaders(responseHeaders(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE.length()))
        .withBody(EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE)
      ));

    // TODO: Here we should set some user to add it as search parameter, but this
    // leads to an exception in the deepness of the URI builder of the base service
    // implementation. I guess that our implementation can't deal with such nested models
    // as search object.
    final var searchObject = UserProfile.builder().build();

    final var result = sut.searchUnique(searchObject);

    assertAll(
      () -> assertThat(result.isEmpty(), is(false)),
      () -> assertThat(result.get().getUser(), is(nullValue()))
    );
  }

  @Test
  void searchUnique_withQueryParams() throws URISyntaxException, JsonProcessingException {
    // Here we only test that the object properties are correctly mapped to get params,
    // since the response parsing and binding is covered by the other tests.
    stubFor(get(urlPathEqualTo("/api/v2/user_profile/"))
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

    assertAll(
      () -> assertThat(result.isEmpty(), is(false)),
      () -> assertThat(result.get().getUser(), is(nullValue()))
    );
  }

  @Test
  void create() {
    final var json = """
      {
        "user" : {
          "id" : 42,
          "username" : "alf",
          "first_name" : "Gordon",
          "last_name" : "Shumway"
        }
      }
      """;
    stubFor(post(urlPathEqualTo("/api/v2/user_profile/"))
      .withRequestBody(equalToJson(json))
      .willReturn(created()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json) // Typically the entity with new assigned id is returned, but we ignore this here.
      ));
    final var toCreate = UserProfile.builder()
      .user(User.builder()
        .id(42)
        .username("alf")
        .firstName("Gordon")
        .lastName("Shumway")
        .build())
      .build();

    final var result = sut.create(toCreate);

    assertThat(result, is(toCreate));
  }

  @Test
  void delete_byId() {
    stubFor(delete(urlPathEqualTo("/api/v2/user_profile/42/"))
      .willReturn(ok()));

    sut.delete(42L);

    verify(deleteRequestedFor(urlPathEqualTo("/api/v2/user_profile/42/")));
  }

  @Test
  void update() {
    final var json = """
      {
        "user" : {
          "id" : 42,
          "username" : "alf",
          "first_name" : "Gordon",
          "last_name" : "Shumway"
        }
      }
      """;
    stubFor(put(urlPathEqualTo("/api/v2/user_profile/42/"))
      .withRequestBody(equalToJson(json))
      .willReturn(ok()
        .withHeaders(responseHeaders(json.length()))
        .withBody(json)
      ));

    final var toUpdate = UserProfile.builder()
      .user(User.builder()
        .id(42)
        .username("alf")
        .firstName("Gordon")
        .lastName("Shumway")
        .build())
      .build();

    final var result = sut.update(toUpdate, 42L);

    assertThat(result, is(toUpdate));
  }
}
