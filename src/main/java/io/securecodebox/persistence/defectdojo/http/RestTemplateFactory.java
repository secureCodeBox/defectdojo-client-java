// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.http;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Factory to create preconfigured {@link RestTemplate}
 */
@Slf4j
public final class RestTemplateFactory {
  private final ProxyConfig proxyConfig;

  public RestTemplateFactory(@NonNull ProxyConfig proxyConfig) {
    super();
    this.proxyConfig = proxyConfig;
  }

  public RestTemplate createRestTemplate() {
    if (proxyConfig.isComplete()) {
      log.debug("Configure proxy authentication for REST template.");
      // Configuring Proxy Authentication explicitly as it isn't done by default for spring rest templates :(
      final var builder = HttpClientBuilder.create()
        .useSystemProperties()
        .setProxy(createHttpHost())
        .setDefaultCredentialsProvider(new ProxyCredentialFactory(proxyConfig).createCredentialsProvider())
        .setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

      final var factory = new HttpComponentsClientHttpRequestFactory();
      factory.setHttpClient(builder.build());

      return new RestTemplate(factory);
    }

    return new RestTemplate();
  }

  HttpHost createHttpHost() {
    return new HttpHost(proxyConfig.getHost(), proxyConfig.getPort());
  }
}
