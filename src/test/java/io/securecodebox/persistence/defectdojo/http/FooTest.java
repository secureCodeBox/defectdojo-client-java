// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.http;

import io.securecodebox.persistence.defectdojo.config.Config;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link Foo}
 */
class FooTest {
  private final Config config = new Config("url", "apikey");
  private final ProxyConfig proxyConfig = ProxyConfig.builder()
    .user("user")
    .password("pw")
    .host("host")
    .port(42)
    .build();
  private final Foo sut = new Foo(config, proxyConfig);

  @Test
  void createCredentialsProvider() {
    final var result = sut.createCredentialsProvider();
    final var credentials = result.getCredentials(sut.createAuthScope());

    assertAll(
      () -> assertThat(credentials.getUserPrincipal().getName(), is(proxyConfig.getUser())),
      () -> assertThat(credentials.getPassword(), is(proxyConfig.getPassword()))
    );
  }

  @Test
  void createAuthScope() {
    final var result = sut.createAuthScope();

    assertAll(
      () -> assertThat(result.getHost(), is(proxyConfig.getHost())),
      () -> assertThat(result.getPort(), is(proxyConfig.getPort()))
    );
  }

  @Test
  void createCredentials() {
    final var result = sut.createCredentials();

    assertAll(
      () -> assertThat(result.getUserPrincipal().getName(), is(proxyConfig.getUser())),
      () -> assertThat(result.getPassword(), is(proxyConfig.getPassword()))
    );
  }

  @Test
  void createHttpHost() {
    final var result = sut.createHttpHost();

    assertAll(
      () -> assertThat(result.getHostName(), is(proxyConfig.getHost())),
      () -> assertThat(result.getPort(), is(proxyConfig.getPort()))
    );
  }
}
