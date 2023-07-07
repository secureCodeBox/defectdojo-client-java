package io.securecodebox.persistence.defectdojo.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link DefaultHttpRequesterTest}
 */
final class DefaultHttpRequesterTest {
    /**
     * Since System.getProperty() is a side effect we need to back up and restore it to isolate test cases.
     */
    private final Map<ImportScanService.ProxyConfigNames, String> backup = new HashMap<>();
    private final DefaultImportScanService.DefaultHttpRequester sut = new DefaultImportScanService.DefaultHttpRequester();

    @BeforeEach
    void backupSystemProperties() {
        backup.clear();
        for (final var name : ImportScanService.ProxyConfigNames.values()) {
            backup.put(name, System.getProperty(name.getLiterat()));
        }
    }

    @AfterEach
    void restoreSystemProperties() {
        for (final var entry : backup.entrySet()) {
            final var name = entry.getKey().getLiterat();

            if (null == entry.getValue()) {
                System.clearProperty(name);
            } else {
                System.setProperty(name, entry.getValue());
            }
        }
    }

    @Test
    void shouldConfigureProxySettings_falseIfNeitherUserNorPasswordIsSet() {
        System.clearProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat());
        System.clearProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat());

        assertThat(sut.shouldConfigureProxySettings(), is(false));
    }

    @Test
    void shouldConfigureProxySettings_falseIfUserSetButPasswordNot() {
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.clearProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat());

        assertThat(sut.shouldConfigureProxySettings(), is(false));
    }

    @Test
    void shouldConfigureProxySettings_falseIfPasswordSetButUserNot() {
        System.clearProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat());
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");

        assertThat(sut.shouldConfigureProxySettings(), is(false));
    }

    @Test
    void shouldConfigureProxySettings_trueIfUserAndPasswordSet() {
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");

        assertThat(sut.shouldConfigureProxySettings(), is(true));
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfUserNotSet() {
        System.clearProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat());
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "4242");

        final var thrown = assertThrows(
            DefaultImportScanService.MissingProxyAuthenticationConfig.class,
            sut::createRequestFactoryWithProxyAuthConfig);

        assertThat(thrown.getMessage(), is("Expected System property 'http.proxyUser' not set!"));
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfPasswordNotSet() {
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.clearProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat());
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "4242");

        final var thrown = assertThrows(
            DefaultImportScanService.MissingProxyAuthenticationConfig.class,
            sut::createRequestFactoryWithProxyAuthConfig);

        assertThat(thrown.getMessage(), is("Expected System property 'http.proxyPassword' not set!"));
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfHostNotSet() {
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        System.clearProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_HOST.getLiterat());
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "4242");

        final var thrown = assertThrows(
            DefaultImportScanService.MissingProxyAuthenticationConfig.class,
            sut::createRequestFactoryWithProxyAuthConfig);

        assertThat(thrown.getMessage(), is("Expected System property 'http.proxyHost' not set!"));
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfPortNotSet() {
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        System.clearProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PORT.getLiterat());

        final var thrown = assertThrows(
            DefaultImportScanService.MissingProxyAuthenticationConfig.class,
            sut::createRequestFactoryWithProxyAuthConfig);

        assertThat(thrown.getMessage(), is("Expected System property 'http.proxyPort' not set!"));
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfPortIsNotInteger() {
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        System.setProperty(ImportScanService.ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "FUBAR");

        final var thrown = assertThrows(
            IllegalArgumentException.class,
            sut::createRequestFactoryWithProxyAuthConfig);

        assertThat(
            thrown.getMessage(),
            is("Given port for proxy authentication configuration (property 'http.proxyPort') is not a valid number! Given value wa 'FUBAR'."));
    }
}
