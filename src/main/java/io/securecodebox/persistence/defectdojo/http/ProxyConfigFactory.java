package io.securecodebox.persistence.defectdojo.http;

import lombok.Getter;
import lombok.NonNull;

/**
 * This class is responsible to create a proxy configuration
 * <p>
 * This implementation collects the configuration values from Java system properties. It also treats the
 * cases of non-present values ot values of incorrect type.
 * </p>
 * <p>
 * This class does not validate for semantic errors of the values, e.g. malformed hostnames or invalid
 * port numbers.
 * </p>
 */
public final class ProxyConfigFactory {
    private final SystemPropertyFinder properties = new SystemPropertyFinder();

    public ProxyConfig create() {
        final var builder = ProxyConfig.builder();

        if (properties.notHasProperty(ProxyConfigNames.HTTP_PROXY_USER)) {
            throw new MissingProxyConfigValue(ProxyConfigNames.HTTP_PROXY_USER);
        }

        builder.user(properties.getProperty(ProxyConfigNames.HTTP_PROXY_USER));

        if (properties.notHasProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD)) {
            throw new MissingProxyConfigValue(ProxyConfigNames.HTTP_PROXY_PASSWORD);
        }

        builder.password(properties.getProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD));

        if (properties.notHasProperty(ProxyConfigNames.HTTP_PROXY_HOST)) {
            throw new MissingProxyConfigValue(ProxyConfigNames.HTTP_PROXY_HOST);
        }

        builder.host(properties.getProperty(ProxyConfigNames.HTTP_PROXY_HOST));

        if (properties.notHasProperty(ProxyConfigNames.HTTP_PROXY_PORT)) {
            throw new MissingProxyConfigValue(ProxyConfigNames.HTTP_PROXY_PORT);
        }

        try {
            builder.port(Integer.parseInt(properties.getProperty(ProxyConfigNames.HTTP_PROXY_PORT)));
        } catch (final NumberFormatException e) {
            throw new IllegalArgumentException(
                String.format("Given port for proxy authentication configuration (property '%s') is not a valid number! Given value wa '%s'.",
                    ProxyConfigNames.HTTP_PROXY_PORT.getLiterat(),
                    System.getProperty("http.proxyPort")),
                e);
        }

        return builder.build();
    }

    /**
     * These properties can be configured by passing them to the running Java process w/ flag {@literal -D}
     * <p>
     * Example: {@literal java -Dhttp.proxyHost=... -D... -jar ...}
     * </p>
     * <p>
     * <strong>Important</strong>: All four parameters are mandatory. You must set them all
     * or none of them!
     * </p>
     */
    @Getter
    public enum ProxyConfigNames {
        /**
         * System property name for the proxy username
         */
        HTTP_PROXY_USER("http.proxyUser"),
        /**
         * System property name for the proxy user's password
         */
        HTTP_PROXY_PASSWORD("http.proxyPassword"),
        /**
         * System property name for the proxy's hostname
         */
        HTTP_PROXY_HOST("http.proxyHost"),
        /**
         * System property for the proxy's port number
         */
        HTTP_PROXY_PORT("http.proxyPort");

        private final String literat;

        ProxyConfigNames(String literat) {
            this.literat = literat;
        }
    }

    private static class SystemPropertyFinder {
        private boolean hasProperty(@NonNull final ProxyConfigNames name) {
            return System.getProperty(name.getLiterat()) != null;
        }

        private boolean notHasProperty(@NonNull final ProxyConfigNames name) {
            return !hasProperty(name);
        }

        private String getProperty(@NonNull final ProxyConfigNames name) {
            return System.getProperty(name.getLiterat());
        }
    }

    /**
     * This exception indicates a missing proxy config value
     */
    final static class MissingProxyConfigValue extends RuntimeException {
        MissingProxyConfigValue(ProxyConfigNames name) {
            super(String.format("Expected System property '%s' not set!", name.getLiterat()));
        }
    }
}
