// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.http;

import io.securecodebox.persistence.defectdojo.config.Config;
import lombok.NonNull;
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

    @Test
    void generateAuthorizationHeaders_withoutProxyAuth() {
        final var incompleteProxyConfig = ProxyConfig.NULL;

        final var sut = new Foo(config, incompleteProxyConfig);

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
        final var completeProxyConfig = ProxyConfig.builder()
            .user("user")
            .password("pw")
            .host("host")
            .port(42)
            .build();

        final var sut = new Foo(config, completeProxyConfig);

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

        assertThat(Foo.encodeProxyCredentials(proxyConfig), is("YsOkcnRyYW06Z29oemU4QWU="));
    }
}
