package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.ToolConfig;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link ToolConfigService}
 */
final class ToolConfigServiceTest extends WireMockBaseTestCase {
  private final ToolConfigService sut = new ToolConfigService(conf());

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/tool_configurations/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readResponseBodyFromFixture("ToolConfigService_response_fixture.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(1)),
      () -> assertThat(result, containsInAnyOrder(
        ToolConfig.builder()
          .id(1)
          .name("secureCodeBox")
          .description("secureCodeBox is a kubernetes based, modularized toolchain for continuous security scans of your software project.")
          .url("https://github.com/secureCodeBox")
          .toolType(7)
          .build()
      ))
    );
  }
}
