// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import lombok.Data;

/**
 * DTO to upload a secureCodeBox scan file
 * <p>
 * This is not a JSON model – thus not implementing the Model interface – because it is only used as DTO for the
 * multi-part form upload.
 * </p>
 */
@Data
public final class ScanFile {
  /**
   * A default name must be set
   * <p>
   * It does not matter however unless the parser pays attention to file endings like json or xml.
   * </p>
   */
  private static final String DEFAULT_NAME = "default-name.txt";

  /**
   * The file content.
   */
  private String content;

  /**
   * Name of file.
   * <p>
   * Defaults to {@link #DEFAULT_NAME}.
   * </p>
   */
  private String name;

  public ScanFile(String content) {
    this(content, DEFAULT_NAME);
  }

  public ScanFile(String content, String name) {
    super();
    this.content = content;
    this.name = name;
  }

}
