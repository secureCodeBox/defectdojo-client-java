// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.User;
import io.securecodebox.persistence.defectdojo.model.UserProfile;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


/**
 * Tests for UserProfileService
 * <p>
 * This test is special because the defectdojo api does not return a list, but the generic code assumes every endpoint
 * returns a list.
 * </p>
 *
 * TODO: Add WireMock integration test.
 */
class UserProfileServiceTest {
  /**
   * This string does not contain every field of the api response as those are not implemented
   */
  private static final String API_RESPONSE = """
    {
      "user": {
        "id": 0,
        "username": "username",
        "first_name": "first_name",
        "last_name": "last_name",
        "email": "user@example.com",
        "last_login": "2022-11-01T16:20:19.373Z",
        "is_active": true,
        "is_superuser": true,
        "configuration_permissions": [0]
      }
    }
    """;

  private final Config config = new Config("https://defectdojo.example.com", "abc", 42);
  private final UserProfileService sut = new UserProfileService(config);
  private final MockRestServiceServer server = MockRestServiceServer.createServer(sut.getRestTemplate());

  @Test
  void search() throws JsonProcessingException, URISyntaxException {
    final var url = String.format("%s/api/v2/%s/?offset=0&limit=100", config.getUrl(), sut.getUrlPath());
    server.expect(requestTo(url)).andRespond(withSuccess(API_RESPONSE, MediaType.APPLICATION_JSON));

    final var expected = new UserProfile(new User(0L, "username", "first_name", "last_name"));

    assertIterableEquals(List.of(expected), sut.search());
    server.verify();
  }
}
