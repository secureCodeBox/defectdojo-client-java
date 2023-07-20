// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;
import uk.org.webcompere.systemstubs.properties.SystemProperties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link DefaultHttpRequesterTest}
 */
@ExtendWith(SystemStubsExtension.class)
final class DefaultHttpRequesterTest {
    @SystemStub

    private SystemProperties systemProperties;
    private final DefaultImportScanService.DefaultHttpRequester sut = new DefaultImportScanService.DefaultHttpRequester();

    @Test
    void shouldConfigureProxySettings_falseIfNeitherUserNorPasswordIsSet() {
        assertThat(sut.shouldConfigureProxySettings(), is(false));
    }

    @Test
    void shouldConfigureProxySettings_falseIfUserSetButPasswordNot() throws Exception {
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");

        systemProperties.execute(() -> {
            assertThat(sut.shouldConfigureProxySettings(), is(false));
        });
    }

    @Test
    void shouldConfigureProxySettings_falseIfPasswordSetButUserNot() throws Exception {
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");

        systemProperties.execute(() -> {
            assertThat(sut.shouldConfigureProxySettings(), is(false));
        });
    }

    @Test
    void shouldConfigureProxySettings_trueIfUserAndPasswordSet() throws Exception {
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");

        systemProperties.execute(() -> {
            assertThat(sut.shouldConfigureProxySettings(), is(true));
        });
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfUserNotSet() throws Exception {
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "4242");

        systemProperties.execute(() -> {
            final var thrown = assertThrows(
                DefaultImportScanService.MissingProxyAuthenticationConfig.class,
                sut::createRequestFactoryWithProxyAuthConfig);

            assertThat(thrown.getMessage(), is("Expected System property 'http.proxyUser' not set!"));
        });
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfPasswordNotSet() throws Exception {
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "4242");

        systemProperties.execute(() -> {
            final var thrown = assertThrows(
                DefaultImportScanService.MissingProxyAuthenticationConfig.class,
                sut::createRequestFactoryWithProxyAuthConfig);

            assertThat(thrown.getMessage(), is("Expected System property 'http.proxyPassword' not set!"));
        });
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfHostNotSet() throws Exception {
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "4242");

        systemProperties.execute(() -> {
            final var thrown = assertThrows(
                DefaultImportScanService.MissingProxyAuthenticationConfig.class,
                sut::createRequestFactoryWithProxyAuthConfig);

            assertThat(thrown.getMessage(), is("Expected System property 'http.proxyHost' not set!"));
        });
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfPortNotSet() throws Exception {
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");

        systemProperties.execute(() -> {
            final var thrown = assertThrows(
                DefaultImportScanService.MissingProxyAuthenticationConfig.class,
                sut::createRequestFactoryWithProxyAuthConfig);

            assertThat(thrown.getMessage(), is("Expected System property 'http.proxyPort' not set!"));
        });
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfPortIsNotInteger() throws Exception {
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        systemProperties.set(ImportScanService.ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "FUBAR");

        systemProperties.execute(() -> {
            final var thrown = assertThrows(
                IllegalArgumentException.class,
                sut::createRequestFactoryWithProxyAuthConfig);

            assertThat(
                thrown.getMessage(),
                is("Given port for proxy authentication configuration (property 'http.proxyPort') is not a valid number! Given value wa 'FUBAR'."));
        });
    }
}
