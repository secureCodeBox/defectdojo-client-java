// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.http.ProxyConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link DefaultImportScanService}
 *
 * TODO: Add WireMock integration test.
 */
class DefaultImportScanServiceTest {
  private final Config config = new Config(
    "http://localhost",
    "apiKey",
    23
  );
  private final DefaultImportScanService sut = new DefaultImportScanService(config, ProxyConfig.NULL);

  @Test
  void constructorShouldThrowExceptionOnNullConfig() {
    assertThrows(NullPointerException.class, () -> {
      new DefaultImportScanService(null, ProxyConfig.NULL);
    });
  }

  @Test
  void constructorShouldThrowExceptionOnNullProxyConfig() {
    assertThrows(NullPointerException.class, () -> {
      new DefaultImportScanService(Config.NULL, null);
    });
  }

  @Test
  void createDefectDojoAuthorizationHeaders_apiKeyFromConfigShouldBePresentAsAuthHEader() {
    final var authorizationHeaders = sut.createDefectDojoAuthorizationHeaders();
    assertAll(
      () -> assertThat(authorizationHeaders.size(), is(1)),
      () -> assertThat(authorizationHeaders.get(HttpHeaders.AUTHORIZATION).get(0), is("Token apiKey"))
    );
  }

  @Test
  void shouldConfigureProxySettings_trueIfProxyConfigIsComplete() {
    final var proxyConfig = ProxyConfig.builder()
      .user("user")
      .password("pw")
      .host("host")
      .port(42)
      .build();
    final var innerSut = new DefaultImportScanService(config, proxyConfig);

    assertThat(innerSut.shouldConfigureProxySettings(), is(true));
  }

  @Test
  void shouldConfigureProxySettings_falseIfProxyConfigIsIncomplete() {
    final var proxyConfig = ProxyConfig.builder()
      .build();
    final var innerSut = new DefaultImportScanService(config, proxyConfig);

    assertThat(innerSut.shouldConfigureProxySettings(), is(false));
  }

  @Test
  void generateApiUrl() {
    assertThat(sut.generateApiUrl("foo"), is("http://localhost/api/v2/foo/"));
  }

  @Test
  @Disabled("Not implemented yet")
  void importScan_shouldPassImportScanAsEndpoint() {
  }

  @Test
  @Disabled("Not implemented yet")
  void importScan_shouldPassEngagementIdAsEngagement() {
  }

  @Test
  @Disabled("Not implemented yet")
  void reimportScan_shouldPassReimportScanAsEndpoint() {
  }

  @Test
  @Disabled("Not implemented yet")
  void reimportScan_shouldPassEngagementIdAsTest() {
  }
}
