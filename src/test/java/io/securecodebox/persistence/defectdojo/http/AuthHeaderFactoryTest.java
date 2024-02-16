package io.securecodebox.persistence.defectdojo.http;

import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link AuthHeaderFactory}
 */
class AuthHeaderFactoryTest {
  private final ClientConfig clientConfig = new ClientConfig("url", "apikey");
  private final AuthHeaderFactory sut = new AuthHeaderFactory(clientConfig);

  @Test
  void setProxyConfig_doesNotAllowNull() {
    assertThrows(NullPointerException.class, () -> sut.setProxyConfig(null));
  }

  @Test
  void generateAuthorizationHeaders_withoutProxyAuth() {
    assertAll(
      () -> assertThat(
        sut.generateAuthorizationHeaders().get(HttpHeaders.AUTHORIZATION),
        contains("Token apikey")),
      () -> assertThat(
        sut.generateAuthorizationHeaders().get(HttpHeaders.PROXY_AUTHORIZATION),
        not(contains("Basic dXNlcjpwdw==")))
    );
  }

  @Test
  void generateAuthorizationHeaders_withProxyAuth() {
    final ProxyConfig proxyConfig = ProxyConfig.builder()
      .user("user")
      .password("pw")
      .host("host")
      .port(42)
      .build();
    sut.setProxyConfig(proxyConfig);

    assertAll(
      () -> assertThat(
        sut.generateAuthorizationHeaders().get(HttpHeaders.AUTHORIZATION),
        contains("Token apikey")),
      () -> assertThat(
        sut.generateAuthorizationHeaders().get(HttpHeaders.PROXY_AUTHORIZATION),
        contains("Basic dXNlcjpwdw=="))
    );
  }

  @Test
  void encodeProxyCredentials() {
    final var proxyConfig = ProxyConfig.builder()
      .user("bärtram")
      .password("gohze8Ae")
      .build();

    assertThat(sut.encodeProxyCredentials(proxyConfig), is("YsOkcnRyYW06Z29oemU4QWU="));
  }
}
