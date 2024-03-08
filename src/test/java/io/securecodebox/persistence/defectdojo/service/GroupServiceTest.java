// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.Group;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link GroupService}
 */
final class GroupServiceTest extends WireMockBaseTestCase {
  private final GroupService sut = new GroupService(conf());

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/dojo_groups/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readFixtureFile("GroupService_response_fixture.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(3)),
      () -> assertThat(result, containsInAnyOrder(
        Group.builder()
          .id(1)
          .name("foo")
          .socialProvider("GitHub")
          .users(List.of(4L))
          .build(),
        Group.builder()
          .id(2)
          .name("bar")
          .socialProvider("GitHub")
          .users(List.of(1L, 2L, 3L))
          .build(),
        Group.builder()
          .id(3)
          .name("snafu")
          .socialProvider("GitHub")
          .users(List.of(4L, 5L))
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
