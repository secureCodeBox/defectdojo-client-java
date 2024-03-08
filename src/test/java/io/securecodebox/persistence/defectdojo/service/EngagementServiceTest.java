// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.Engagement;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link EngagementService}
 */
final class EngagementServiceTest extends WireMockBaseTestCase {
  private final EngagementService sut = new EngagementService(conf());

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/engagements/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readFixtureFile("EngagementService_response_fixture.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(3)),
      () -> assertThat(result, containsInAnyOrder(
        Engagement.builder()
          .id(806)
          .branch("")
          .name("nmap-vienna-client-1709886900")
          .description("")
          .version("")
          .targetStart("2024-03-08")
          .targetEnd("2024-03-08")
          .status(Engagement.Status.IN_PROGRESS)
          .engagementType("CI/CD")
          .lead(3)
          .product(162)
          .orchestrationEngine(1)
          .tags(Collections.emptyList())
          .build(),
        Engagement.builder()
          .id(807)
          .branch("")
          .name("nmap-stuttgart-client-1709886900")
          .description("")
          .version("")
          .targetStart("2024-03-08")
          .targetEnd("2024-03-08")
          .status(Engagement.Status.IN_PROGRESS)
          .engagementType("CI/CD")
          .lead(3)
          .product(139)
          .orchestrationEngine(1)
          .tags(Collections.emptyList())
          .build(),
        Engagement.builder()
          .id(808)
          .branch("")
          .name("nmap-frankfurt-client-1709886900")
          .description("")
          .version("")
          .targetStart("2024-03-08")
          .targetEnd("2024-03-08")
          .status(Engagement.Status.IN_PROGRESS)
          .engagementType("CI/CD")
          .lead(3)
          .product(140)
          .orchestrationEngine(1)
          .tags(Collections.emptyList())
          .build()
      ))
    );
  }
}
