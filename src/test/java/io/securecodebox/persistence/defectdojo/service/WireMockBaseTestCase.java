package io.securecodebox.persistence.defectdojo.service;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.securecodebox.persistence.defectdojo.config.Config;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * Base test class for WireMock test setups
 */
@Getter
@Accessors(fluent = true)
@WireMockTest(httpPort = WireMockBaseTestCase.PORT)
abstract class WireMockBaseTestCase {
  static final int PORT = 8888;
  private static final String FIXTURE_BASE_PACKAGE = "io/securecodebox/persistence/defectdojo/service";

  private final Config conf = new Config(
    String.format("http://localhost:%d/", PORT),
    "not-required-for-tests");

  String readFixtureFile(String fixtureFile) throws IOException {
    final var fixtureFilePath = FIXTURE_BASE_PACKAGE + "/" + fixtureFile;

    try (final var input = getClass().getClassLoader().getResourceAsStream(fixtureFilePath)) {
      final var bytes = Objects.requireNonNull(input).readAllBytes();
      return new String(bytes, StandardCharsets.UTF_8);
    }
  }
}
