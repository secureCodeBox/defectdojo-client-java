// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.http;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link RestTemplateFactory}
 */
class RestTemplateFactoryTest {
  private final ProxyConfig proxyConfig = ProxyConfig.builder()
    .user("user")
    .password("pw")
    .host("host")
    .port(42)
    .build();
  private final RestTemplateFactory sut = new RestTemplateFactory(proxyConfig);



  @Test
  void createHttpHost() {
    final var result = sut.createHttpHost();

    assertAll(
      () -> assertThat(result.getHostName(), is(proxyConfig.getHost())),
      () -> assertThat(result.getPort(), is(proxyConfig.getPort()))
    );
  }
}
