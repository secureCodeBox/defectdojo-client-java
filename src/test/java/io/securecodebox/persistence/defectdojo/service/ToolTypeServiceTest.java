package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.ToolType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link ToolTypeService}
 */
final class ToolTypeServiceTest extends WireMockBaseTestCase {
  private final ToolTypeService sut = new ToolTypeService(conf());

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/tool_types/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readFixtureFile("ToolTypeService_response_fixture.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(7)),
      () -> assertThat(result, containsInAnyOrder(
        ToolType.builder()
          .id(6)
          .name("BlackDuck API")
          .build(),
        ToolType.builder()
          .id(3)
          .name("Bugcrowd API")
          .build(),
        ToolType.builder()
          .id(4)
          .name("Cobalt.io")
          .build(),
        ToolType.builder()
          .id(1)
          .name("Edgescan")
          .build(),
        ToolType.builder()
          .id(7)
          .name("Security Test Orchestration Engine")
          .description("Security Test Orchestration Engine")
          .build(),
        ToolType.builder()
          .id(5)
          .name("SonarQube")
          .build(),
        ToolType.builder()
          .id(2)
          .name("Vulners")
          .build()
      ))
    );
  }
}
