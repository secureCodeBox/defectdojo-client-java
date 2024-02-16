package io.securecodebox.persistence.defectdojo.http;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link ProxyCredentialFactory}
 */
class ProxyCredentialFactoryTest {
  private final ProxyConfig config = ProxyConfig.builder()
    .user("user")
    .password("pw")
    .host("host")
    .port(42)
    .build();
  private final ProxyCredentialFactory sut = new ProxyCredentialFactory(config);

  @Test
  void createCredentialsProvider() {
    final var result = sut.createCredentialsProvider();
    final var credentials = result.getCredentials(sut.createAuthScope());

    assertAll(
      () -> assertThat(credentials.getUserPrincipal().getName(), is(config.getUser())),
      () -> assertThat(credentials.getPassword(), is(config.getPassword()))
    );
  }

  @Test
  void createAuthScope() {
    final var result = sut.createAuthScope();

    assertAll(
      () -> assertThat(result.getHost(), is(config.getHost())),
      () -> assertThat(result.getPort(), is(config.getPort()))
    );
  }

  @Test
  void createCredentials() {
    final var result = sut.createCredentials();

    assertAll(
      () -> assertThat(result.getUserPrincipal().getName(), is(config.getUser())),
      () -> assertThat(result.getPassword(), is(config.getPassword()))
    );
  }
}
