// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.User;
import io.securecodebox.persistence.defectdojo.model.UserProfile;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;


/**
 * Tests for {@link UserProfileService}
 * <p>
 * This test is special because the defectdojo api does not return a list, but the generic code assumes every endpoint
 * returns a list.
 * </p>
 */
final class UserProfileServiceTest extends WireMockBaseTestCase {
  private final UserProfileService sut = new UserProfileService(conf());

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/user_profile/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readFixtureFile("UserProfileService_response_fixture.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(1)),
      () -> assertThat(result, containsInAnyOrder(
        UserProfile.builder()
          .user(new User(0L, "username", "first_name", "last_name"))
          .build()
      ))
    );
  }

  @Test
  @Disabled("TODO: Implement test.")
  void search_withQueryParams() {
  }

  @Test
  @Disabled("TODO: Implement test.")
  void get_byId() {
  }


  @Test
  @Disabled("TODO: Implement test.")
  void searchUnique_withSearchObject() {
  }

  @Test
  @Disabled("TODO: Implement test.")
  void searchUnique_withQueryParams() {
  }

  @Test
  @Disabled("TODO: Implement test.")
  void create() {
  }

  @Test
  @Disabled("TODO: Implement test.")
  void delete() {
  }

  @Test
  @Disabled("TODO: Implement test.")
  void update() {
  }
}
