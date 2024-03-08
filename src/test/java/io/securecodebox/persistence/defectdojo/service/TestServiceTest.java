
// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
//
package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.Test;
import org.junit.jupiter.api.Disabled;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link TestService}
 */
final class TestServiceTest extends WireMockBaseTestCase {
  private final TestService sut = new TestService(conf());

  @org.junit.jupiter.api.Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/tests/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readFixtureFile("TestService_response_fixture.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(3)),
      () -> assertThat(result, containsInAnyOrder(
        Test.builder()
          .id(1)
          .title("nuclei-owasp.org-1709280838-nmap-hostscan-6ckh4-nmap-su466fl")
          .description("# Nuclei Scan\nStarted: 01.03.2024 10:04:54\nEnded: 01.03.2024 15:06:30\nScanType: nuclei\nParameters: [-disable-update-check,-no-interactsh,-u,api.tokengate.dlt.owasp.org:443]")
          .targetStart("2024-03-01T10:04:54Z")
          .targetEnd("2024-03-01T15:06:29Z")
          .testType(67)
          .lead(3)
          .percentComplete(100)
          .engagement(1)
          .environment(1)
          .build(),
        Test.builder()
          .id(2)
          .title("nuclei-owasp.org-1709280838-nmap-hostscan-7xd2c-nuclei-5gzps")
          .description("# Nuclei Scan\nStarted: 01.03.2024 08:47:33\nEnded: 01.03.2024 15:06:34\nScanType: nuclei\nParameters: [-disable-update-check,-no-interactsh,-u,api.tokengate-dev.dlt.owasp.org]")
          .targetStart("2024-03-01T08:47:33Z")
          .targetEnd("2024-03-01T15:06:34Z")
          .testType(42)
          .lead(23)
          .percentComplete(43)
          .engagement(2)
          .environment(3)
          .build(),
        Test.builder()
          .id(3)
          .title("nuclei-owasp.org-1709280838-nmap-hostscan-6ckh4-nmap-sub6l7l")
          .description("# Nuclei Scan\nStarted: 01.03.2024 10:04:54\nEnded: 01.03.2024 15:06:35\nScanType: nuclei\nParameters: [-disable-update-check,-no-interactsh,-u,api.tokengate.dlt.owasp.org]")
          .targetStart("2024-03-01T10:04:54Z")
          .targetEnd("2024-03-01T15:06:35Z")
          .testType(67)
          .lead(3)
          .percentComplete(100)
          .engagement(1)
          .environment(1)
          .build()
      ))
    );
  }

  @org.junit.jupiter.api.Test
  @Disabled("TODO: Implement test.")
  void search_withQueryParams() {
  }

  @org.junit.jupiter.api.Test
  @Disabled("TODO: Implement test.")
  void get_byId() {
  }


  @org.junit.jupiter.api.Test
  @Disabled("TODO: Implement test.")
  void searchUnique_withSearchObject() {
  }

  @org.junit.jupiter.api.Test
  @Disabled("TODO: Implement test.")
  void searchUnique_withQueryParams() {
  }

  @org.junit.jupiter.api.Test
  @Disabled("TODO: Implement test.")
  void create() {
  }

  @org.junit.jupiter.api.Test
  @Disabled("TODO: Implement test.")
  void delete() {
  }

  @org.junit.jupiter.api.Test
  @Disabled("TODO: Implement test.")
  void update() {
  }
}
