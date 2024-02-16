// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.exception;

/**
 * Indicates a missing/bad {@link io.securecodebox.persistence.defectdojo.config.Config config property}
 */
public final class ConfigException extends PersistenceException {
  public ConfigException(final String message) {
    super(message);
  }

  public ConfigException(final String message, final Throwable cause) {
    super(message, cause);
  }
}
