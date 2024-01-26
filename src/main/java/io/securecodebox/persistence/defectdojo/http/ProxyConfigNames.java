// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.http;

import lombok.Getter;

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
