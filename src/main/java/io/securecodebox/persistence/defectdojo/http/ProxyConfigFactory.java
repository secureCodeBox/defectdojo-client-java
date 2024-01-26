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
  static final ProxyConfig DEFAULT_CONFIG = ProxyConfig.NULL;
  private final SystemPropertyFinder properties = new SystemPropertyFinder();

  /**
   * Creates a configuration based on {@link ProxyConfigNames environment variables}
   * <p>
   * We assume a complete proxy configuration only if {@link ProxyConfigNames#HTTP_PROXY_USER user} and
   * {@link ProxyConfigNames#HTTP_PROXY_PASSWORD password} is configured. Unless an
   * {@link #DEFAULT_CONFIG} configuration will be returned.
   * </p>
   * <p>
   * Throws {@link MissingProxyConfigValue}, if not all configuration values are present.
   * </p>
   *
   * @return never {@code null}
   */
  public ProxyConfig create() {
    if (shouldCreateFromProperties()) {
      return createFromProperties();
    }

    return DEFAULT_CONFIG;
  }

  private boolean shouldCreateFromProperties() {
    return properties.hasProperty(ProxyConfigNames.HTTP_PROXY_USER) &&
      properties.hasProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD);
  }

  private ProxyConfig createFromProperties() {
    final var builder = ProxyConfig.builder()
      .user(properties.getProperty(ProxyConfigNames.HTTP_PROXY_USER))
      .password(properties.getProperty(ProxyConfigNames.HTTP_PROXY_PASSWORD));

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
