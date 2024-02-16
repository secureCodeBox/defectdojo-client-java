package io.securecodebox.persistence.defectdojo.http;

import io.securecodebox.persistence.defectdojo.config.Config;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Utility class to create HTTP authorization headers
 */
@Slf4j
public final class AuthHeaderFactory {
  private final Config config;
  @Setter
  @NonNull
  private ProxyConfig proxyConfig = ProxyConfig.NULL;

  public AuthHeaderFactory(@NonNull Config config) {
    super();
    this.config = config;
  }

  /**
   * This method generates appropriate authorization headers
   *
   * @return never {@code null}
   */
  public HttpHeaders generateAuthorizationHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set(HttpHeaders.AUTHORIZATION, "Token " + this.config.getApiKey());

    if (proxyConfig.isComplete()) {
      log.info("Setting Proxy Auth Header...");
      headers.set(HttpHeaders.PROXY_AUTHORIZATION, "Basic " + encodeProxyCredentials(proxyConfig));
    }

    return headers;
  }

  String encodeProxyCredentials(@NonNull final ProxyConfig cfg) {
    final var credential = String.format("%s:%s", cfg.getUser(), cfg.getPassword());
    return Base64.getEncoder().encodeToString(credential.getBytes(StandardCharsets.UTF_8));
  }

}
