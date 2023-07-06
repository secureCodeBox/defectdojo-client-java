// Copyright 2015-2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.exception;

public class DefectDojoLoopException extends RuntimeException {
  public DefectDojoLoopException(String message) {
    super(message);
  }

  public DefectDojoLoopException(String message, Throwable cause) {
    super(message, cause);
  }
}
