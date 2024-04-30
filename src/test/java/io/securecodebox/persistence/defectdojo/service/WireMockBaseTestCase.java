// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import com.github.tomakehurst.wiremock.http.HttpHeader;
import com.github.tomakehurst.wiremock.http.HttpHeaders;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import lombok.Getter;
import lombok.experimental.Accessors;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Base test class for WireMock test setups
 */
@Getter
@Accessors(fluent = true)
@WireMockTest(httpPort = WireMockBaseTestCase.PORT)
abstract class WireMockBaseTestCase {
  static final int PORT = 8888;
  static final String API_KEY = "not-required-for-tests";
  static final String EMPTY_SEARCH_RESULT_RESPONSE_FIXTURE = """
    {
      "count": 0,
      "next": null,
      "previous": null,
      "results": [],
      "prefetch": {}
    }
    """;
  private static final String FIXTURE_BASE_PACKAGE = "io/securecodebox/persistence/defectdojo/service";

  private final ClientConfig conf = new ClientConfig(String.format("http://localhost:%d/", PORT), API_KEY);

  String readFixtureFile(String fixtureFile) throws IOException {
    final var fixtureFilePath = FIXTURE_BASE_PACKAGE + "/" + fixtureFile;

    try (final var input = getClass().getClassLoader().getResourceAsStream(fixtureFilePath)) {
      final var bytes = Objects.requireNonNull(input).readAllBytes();
      return new String(bytes, StandardCharsets.UTF_8);
    }
  }

  HttpHeaders responseHeaders(int contentLength) {
    return HttpHeaders.noHeaders().plus(
      new HttpHeader("date", now()),
      new HttpHeader("content-type", "application/json"),
      new HttpHeader("content-length", String.valueOf(contentLength)),
      new HttpHeader("allow", "GET, PUT, PATCH, DELETE, HEAD, OPTIONS"),
      new HttpHeader("x-frame-options", "DENY"),
      new HttpHeader("x-content-type-options", "nosniff"),
      new HttpHeader("referrer-policy", "same-origin"),
      new HttpHeader("cross-origin-opener-policy", "same-origin"),
      new HttpHeader("vary", "Cookie"),
      new HttpHeader("strict-transport-security", "max-age=31536000; includeSubDomains")
    );
  }

  String now() {
    return ZonedDateTime.now(ZoneId.of("Europe/Berlin"))
      .format(DateTimeFormatter.RFC_1123_DATE_TIME);
  }
}
