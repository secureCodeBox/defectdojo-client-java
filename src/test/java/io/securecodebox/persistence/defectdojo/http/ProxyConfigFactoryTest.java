package io.securecodebox.persistence.defectdojo.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;
import uk.org.webcompere.systemstubs.properties.SystemProperties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Tests for {@link ProxyConfigFactory}
 */
@ExtendWith(SystemStubsExtension.class)
class ProxyConfigFactoryTest {
    @SystemStub
    private SystemProperties restoreSystemProperties;
    private final ProxyConfigFactory sut = new ProxyConfigFactory();

    @Test
    void create_throesExceptionIfUserNotSet() {
        System.clearProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat());
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "4242");

        final var thrown = assertThrows(
            MissingProxyConfigValue.class,
            sut::create);

        assertThat(thrown.getMessage(), containsString("'http.proxyUser'"));
    }

    @Test
    void create_throesExceptionIfPasswordNotSet() {
        System.setProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.clearProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat());
        System.setProperty(ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "4242");

        final var thrown = assertThrows(
            MissingProxyConfigValue.class,
            sut::create);

        assertThat(thrown.getMessage(), containsString("'http.proxyPassword'"));
    }

    @Test
    void create_throesExceptionIfHostNotSet() {
        System.setProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        System.clearProperty(ProxyConfigNames.HTTP_PROXY_HOST.getLiterat());
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "4242");

        final var thrown = assertThrows(
            MissingProxyConfigValue.class,
            sut::create);

        assertThat(thrown.getMessage(), containsString("'http.proxyHost'"));
    }

    @Test
    void create_throesExceptionIfPortNotSet() {
        System.setProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        System.clearProperty(ProxyConfigNames.HTTP_PROXY_PORT.getLiterat());

        final var thrown = assertThrows(
            MissingProxyConfigValue.class,
            sut::create);

        assertThat(thrown.getMessage(), containsString("'http.proxyPort'"));
    }

    @Test
    void create_throesExceptionIfPortIsNotInteger() {
        System.setProperty(ProxyConfigNames.HTTP_PROXY_USER.getLiterat(), "user");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD.getLiterat(), "password");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_HOST.getLiterat(), "host");
        System.setProperty(ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(), "FUBAR");

        final var thrown = assertThrows(
            IllegalArgumentException.class,
            sut::create);

        assertThat(
            thrown.getMessage(),
            is("Given port for proxy authentication configuration (property 'http.proxyPort') is not a valid number! Given value wa 'FUBAR'."));
    }
}