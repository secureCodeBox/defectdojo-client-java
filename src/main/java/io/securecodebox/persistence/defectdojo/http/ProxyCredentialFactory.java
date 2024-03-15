package io.securecodebox.persistence.defectdojo.http;

import lombok.NonNull;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;

/**
 * Utility class to create credentials to authenticate against a HTTP proxy
 */
final class ProxyCredentialFactory {
  private final ProxyConfig proxyConfig;

  ProxyCredentialFactory(@NonNull ProxyConfig proxyConfig) {
    super();
    this.proxyConfig = proxyConfig;
  }

  CredentialsProvider createCredentialsProvider() {
    final var provider = new BasicCredentialsProvider();
    provider.setCredentials(createAuthScope(), createCredentials());
    return provider;
  }

  AuthScope createAuthScope() {
    return new AuthScope(proxyConfig.getHost(), proxyConfig.getPort());
  }

  Credentials createCredentials() {
    return new UsernamePasswordCredentials(proxyConfig.getUser(), proxyConfig.getPassword());
  }
}
