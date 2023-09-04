// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.http;

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

}
