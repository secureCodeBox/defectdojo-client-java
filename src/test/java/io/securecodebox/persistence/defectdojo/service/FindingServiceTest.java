// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.Finding;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link FindingService}
 */
final class FindingServiceTest extends WireMockBaseTestCase {
  private final FindingService sut = new FindingService(conf());

  @Test
  void deserializeList_shouldResultExactlyOneResult() throws IOException {
    var result = sut.deserializeList(readFixtureFile("FindingService_response_fixture.json"));

    assertThat(result.getCount(), is(1));
  }

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/findings/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readFixtureFile("FindingService_response_fixture.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(1)),
      () -> assertThat(result, containsInAnyOrder(
        Finding.builder()
          .id(42)
          .title("Open Port: 9929/TCP")
          .description("IP Address: 198.51.100.0 FQDN: scanme.nmap.org Port/Protocol: 9929/tcp")
          .foundBy(List.of(132L))
          .severity(Finding.Severity.INFORMATIONAL)
          .test(222)
          .mitigation("N/A")
          .impact("No impact provided")
          .verified(true)
          .active(true)
          .endpoints(List.of(875L))
          .createdAt(OffsetDateTime.parse("2021-07-21T12:43:36.549669Z"))
          .acceptedRisks(Collections.emptyList())
          .build()
      ))
    );
  }
}
