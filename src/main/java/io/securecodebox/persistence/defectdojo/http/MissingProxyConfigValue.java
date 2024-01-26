// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.http;

import lombok.NonNull;

/**
 * This exception indicates a missing proxy config value
 */
public final class MissingProxyConfigValue extends RuntimeException {
  MissingProxyConfigValue(@NonNull final ProxyConfigNames name) {
    super(String.format("Expected system property '%s' not set!", name.getLiterat()));
  }
}
