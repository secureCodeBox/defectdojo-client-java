// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.http;

import io.securecodebox.persistence.defectdojo.config.Config;
import lombok.NonNull;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
    void generateAuthorizationHeaders_withoutProxyAuth() {
        final var innerSut = new Foo(config, ProxyConfig.NULL);

        assertAll(
            () -> assertThat(
                innerSut.generateAuthorizationHeaders().get(HttpHeaders.AUTHORIZATION),
                contains("Token apikey")),
            () -> assertThat(
                innerSut.generateAuthorizationHeaders().get(HttpHeaders.PROXY_AUTHORIZATION),
                not(contains("Basic dXNlcjpwdw==")))
        );
    }

    @Test
    void generateAuthorizationHeaders_withProxyAuth() {
        final var innerSut = new Foo(config, proxyConfig);

        assertAll(
            () -> assertThat(
                innerSut.generateAuthorizationHeaders().get(HttpHeaders.AUTHORIZATION),
                contains("Token apikey")),
            () -> assertThat(
                innerSut.generateAuthorizationHeaders().get(HttpHeaders.PROXY_AUTHORIZATION),
                contains("Basic dXNlcjpwdw=="))
        );
    }

    @Test
    void encodeProxyCredentials() {
        final var proxyConfig = ProxyConfig.builder()
            .user("bärtram")
            .password("gohze8Ae")
            .build();

        assertThat(Foo.encodeProxyCredentials(proxyConfig), is("YsOkcnRyYW06Z29oemU4QWU="));
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

    @Test
    void createHttpHost() {
        final var result = sut.createHttpHost();

        assertAll(
                () -> assertThat(result.getHostName(), is(proxyConfig.getHost())),
                () -> assertThat(result.getPort(), is(proxyConfig.getPort()))
        );
    }
}
