// Copyright 2015-2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.exception;

public class LoopException extends RuntimeException {
  public LoopException(String message) {
    super(message);
  }

  public LoopException(String message, Throwable cause) {
    super(message, cause);
  }
}
