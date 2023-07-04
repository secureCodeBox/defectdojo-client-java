package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.service.DefaultImportScanService.MissingProxyAuthenticationConfig;
import io.securecodebox.persistence.defectdojo.service.ImportScanService.ProxyConfigNames;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DefaultImportScanService}
 */
class DefaultImportScanServiceTest {
    private final DefectDojoConfig config = new DefectDojoConfig(
        "url",
        "apiKey",
        "username",
        23,
        42L
    );
    private final DefaultImportScanService sut = new DefaultImportScanService(config);
    /**
     * Since System.getProperty() is an side effect we need to back up and restore it to isolate test cases.
     */
    private final Map<ProxyConfigNames, String> backup = new HashMap<>();

    @BeforeEach
    void backupSystemProperties() {
        backup.clear();
        backup.put(ProxyConfigNames.HTTP_PROXY_HOST, System.getProperty(ProxyConfigNames.HTTP_PROXY_HOST.getLiterat()));
        backup.put(ProxyConfigNames.HTTP_PROXY_PORT, System.getProperty(ProxyConfigNames.HTTP_PROXY_PORT.getLiterat()));
        backup.put(ProxyConfigNames.HTTP_PROXY_USER, System.getProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat()));
        backup.put(ProxyConfigNames.HTTP_PROXY_PASSWORD, System.getProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat()));
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
    void constructorShouldThrowExceptionOnNullConfig() {
        assertThrows(NullPointerException.class, () -> {
            new DefaultImportScanService(null);
        });
    }

    @Test
    void createDefectDojoAuthorizationHeaders_apiKeyFromConfigShouldBePresentAsAuthHEader() {
        final var authorizationHeaders = sut.createDefectDojoAuthorizationHeaders();
        assertAll(
            () -> assertThat(authorizationHeaders.size(), is(1)),
            () -> assertThat(authorizationHeaders.get(HttpHeaders.AUTHORIZATION).get(0), is("Token apiKey"))
        );
    }

    @Test
    void shouldConfigureProxySettings_falseIfNeitherUserNorPasswordIsSet() {
        System.clearProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat());
        System.clearProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat());

        assertThat(sut.shouldConfigureProxySettings(), is(false));
    }

    @Test
    void shouldConfigureProxySettings_falseIfUserSetButPasswordNot() {
        System.setProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.clearProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat());

        assertThat(sut.shouldConfigureProxySettings(), is(false));
    }

    @Test
    void shouldConfigureProxySettings_falseIfPasswordSetButUserNot() {
        System.clearProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat());
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");

        assertThat(sut.shouldConfigureProxySettings(), is(false));
    }

    @Test
    void shouldConfigureProxySettings_trueIfUserAndPasswordSet() {
        System.setProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");

        assertThat(sut.shouldConfigureProxySettings(), is(true));
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfUserNotSet() {
        System.clearProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat());
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "4242");

        final var thrown = assertThrows(
            MissingProxyAuthenticationConfig.class,
            sut::createRequestFactoryWithProxyAuthConfig);

        assertThat(thrown.getMessage(), is("Expected System property 'http.proxyUser' not set!"));
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfPasswordNotSet() {
        System.setProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.clearProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat());
        System.setProperty(ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "4242");

        final var thrown = assertThrows(
            MissingProxyAuthenticationConfig.class,
            sut::createRequestFactoryWithProxyAuthConfig);

        assertThat(thrown.getMessage(), is("Expected System property 'http.proxyPassword' not set!"));
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfHostNotSet() {
        System.setProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        System.clearProperty(ProxyConfigNames.HTTP_PROXY_HOST.getLiterat());
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "4242");

        final var thrown = assertThrows(
            MissingProxyAuthenticationConfig.class,
            sut::createRequestFactoryWithProxyAuthConfig);

        assertThat(thrown.getMessage(), is("Expected System property 'http.proxyHost' not set!"));
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfPortNotSet() {
        System.setProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        System.clearProperty(ProxyConfigNames.HTTP_PROXY_PORT.getLiterat());

        final var thrown = assertThrows(
            MissingProxyAuthenticationConfig.class,
            sut::createRequestFactoryWithProxyAuthConfig);

        assertThat(thrown.getMessage(), is("Expected System property 'http.proxyPort' not set!"));
    }

    @Test
    void createRequestFactoryWithProxyAuthConfig_throesExceptionIfPortIsNotInteger() {
        System.setProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "FUBAR");

        final var thrown = assertThrows(
            IllegalArgumentException.class,
            sut::createRequestFactoryWithProxyAuthConfig);

        assertEquals(
            "Given port for proxy authentication configuration (property 'http.proxyPort') is not a valid number! Given value wa 'FUBAR'.",
            thrown.getMessage());
    }
}
