// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.Endpoint;
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
 * Tests for {@link EndpointService}
 */
final class EndpointServiceTest extends WireMockBaseTestCase {
  private final EndpointService sut = new EndpointService(conf());

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/endpoints/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readFixtureFile("EndpointService_response_fixture.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(5)),
      () -> assertThat(result, containsInAnyOrder(
        Endpoint.builder()
          .id(956)
          .protocol("tcp")
          .host("10.0.0.1")
          .port(80)
          .product(320)
          .build(),
        Endpoint.builder()
          .id(957)
          .protocol("tcp")
          .host("10.0.0.1")
          .port(443)
          .product(320)
          .build(),
        Endpoint.builder()
          .id(961)
          .protocol("tcp")
          .host("10.0.0.2")
          .port(80)
          .product(323)
          .build(),
        Endpoint.builder()
          .id(962)
          .protocol("tcp")
          .host("10.0.0.2")
          .port(443)
          .product(323)
          .build(),
        Endpoint.builder()
          .id(893)
          .protocol("tcp")
          .host("10.0.0.3")
          .port(443)
          .product(296)
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
