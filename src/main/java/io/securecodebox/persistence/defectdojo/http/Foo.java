// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.http;

import io.securecodebox.persistence.defectdojo.config.Config;
import lombok.NonNull;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Placeholder to move duplicated code, will be named better later
 */
public final class Foo {
    private final Config config;
    private final ProxyConfig proxyConfig;

    public Foo(@NonNull final Config config, @NonNull final ProxyConfig proxyConfig) {
        super();
        this.config = config;
        this.proxyConfig = proxyConfig;
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
            // FIXME: System.out logging is a real bad code smell. Standard loging should be used.
            System.out.println("Setting Proxy Auth Header...");
            headers.set(HttpHeaders.PROXY_AUTHORIZATION, "Basic " + encodeProxyCredentials(proxyConfig));
        }

        return headers;
    }

    static String encodeProxyCredentials(@NonNull final ProxyConfig cfg) {
        final var credential = String.format("%s:%s", cfg.getUser(), cfg.getPassword());
        return Base64.getEncoder().encodeToString(credential.getBytes(StandardCharsets.UTF_8));
    }

    public RestTemplate createRestTemplate() {
        RestTemplate restTemplate;

        if (System.getProperty("http.proxyUser") != null && System.getProperty("http.proxyPassword") != null) {
            // Configuring Proxy Authentication explicitly as it isn't done by default for spring rest templates :(
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                new AuthScope(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort"))),
                new UsernamePasswordCredentials(System.getProperty("http.proxyUser"), System.getProperty("http.proxyPassword"))
            );
            HttpClientBuilder clientBuilder = HttpClientBuilder.create();

            clientBuilder.useSystemProperties();
            clientBuilder.setProxy(new HttpHost(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort"))));
            clientBuilder.setDefaultCredentialsProvider(credsProvider);
            clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

            CloseableHttpClient client = clientBuilder.build();

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(client);
            restTemplate = new RestTemplate(factory);
        } else {
            restTemplate = new RestTemplate();
        }

        return restTemplate;
    }
}
