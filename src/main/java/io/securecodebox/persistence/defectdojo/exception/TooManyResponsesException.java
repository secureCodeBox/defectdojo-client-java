// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.exception;

import io.securecodebox.persistence.defectdojo.config.ClientConfig;

/**
 * Thrown if we receive more objects than {@link ClientConfig configured}
 */
public final class TooManyResponsesException extends PersistenceException {
  public TooManyResponsesException(String message) {
    super(message);
  }
}
