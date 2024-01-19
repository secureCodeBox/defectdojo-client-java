// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.User;
import io.securecodebox.persistence.defectdojo.model.UserProfile;

import java.net.URISyntaxException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;


// This test is special because the defectdojo api does not return a list, 
// but the generic code assumes every endpoint returns a list
class UserProfileServiceTest {

  private Config config;
  private UserProfileService underTest;
  private MockRestServiceServer mockServer;

  // This string does not contain every field of the api response as those are not implemented
  private String apiResponse = """
    {
      "user": {
        "id": 0,
        "username": "GdqmXprK.j7R+OYE49SzL3mM2U6I0DyLRHnDg87i9It0AfP-kxvswW3qOI2i+31-@0",
        "first_name": "string",
        "last_name": "string",
        "email": "user@example.com",
        "last_login": "2022-11-01T16:20:19.373Z",
        "is_active": true,
        "is_superuser": true,
        "configuration_permissions": [0]
      }
    }
    """;

  @BeforeEach
  void setup() {
    config = new Config("https://defectdojo.example.com", "abc", 42);
    underTest = new UserProfileService(config);
    mockServer = MockRestServiceServer.createServer(underTest.getRestTemplate());
  }

  @Test
  void testSearch() throws JsonProcessingException, URISyntaxException {
    var url = config.getUrl() + "/api/v2/" + underTest.getUrlPath() + "/?offset=0&limit=100";
    mockServer.expect(requestTo(url)).andRespond(withSuccess(apiResponse, MediaType.APPLICATION_JSON));

    var user = new User(0L, "GdqmXprK.j7R+OYE49SzL3mM2U6I0DyLRHnDg87i9It0AfP-kxvswW3qOI2i+31-@0", "string", "string");
    var userProfile = new UserProfile(user);
    var expected = Arrays.asList(userProfile);
    var actual = underTest.search();

    mockServer.verify();
    assertIterableEquals(expected, actual);
  }
}
