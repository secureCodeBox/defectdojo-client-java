// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.exception;

import io.securecodebox.persistence.defectdojo.config.ClientConfig;

/**
 * Indicates a missing/bad {@link ClientConfig config property}
 */
public final class ClientConfigException extends PersistenceException {
  public ClientConfigException(final String message) {
    super(message);
  }

  public ClientConfigException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
