// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.TestType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link TestTypeService}
 */
final class TestTypeServiceTest extends WireMockBaseTestCase {
  private final TestTypeService sut = new TestTypeService(conf());

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/test_types/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readFixtureFile("TestTypeService_response_fixture.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(9)),
      () -> assertThat(result, containsInAnyOrder(
        TestType.builder()
          .id(99)
          .name("Acunetix360 Scan")
          .staticTool(false)
          .dynamicTool(false)
          .build(),
        TestType.builder()
          .id(19)
          .name("Acunetix Scan")
          .staticTool(false)
          .dynamicTool(false)
          .build(),
        TestType.builder()
          .id(125)
          .name("AnchoreCTL Policies Report")
          .staticTool(false)
          .dynamicTool(false)
          .build(),
        TestType.builder()
          .id(47)
          .name("AnchoreCTL Vuln Report")
          .staticTool(false)
          .dynamicTool(false)
          .build(),
        TestType.builder()
          .id(112)
          .name("Anchore Engine Scan")
          .staticTool(false)
          .dynamicTool(false)
          .build(),
        TestType.builder()
          .id(172)
          .name("Anchore Enterprise Policy Check")
          .staticTool(false)
          .dynamicTool(false)
          .build(),
        TestType.builder()
          .id(106)
          .name("Anchore Grype")
          .staticTool(true)
          .dynamicTool(false)
          .build(),
        TestType.builder()
          .id(1)
          .name("API Test")
          .staticTool(false)
          .dynamicTool(false)
          .build(),
        TestType.builder()
          .id(100)
          .name("AppSpider Scan")
          .staticTool(false)
          .dynamicTool(true)
          .build()
      ))
    );
  }
}
