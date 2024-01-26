// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import lombok.Data;

// TODO: Why we do not have as many annotations as the other models here?
// TODO: Why does this class does not implement Model?
@Data
public final class ScanFile {
  /**
   * A default name must be set
   * <p>
   * It does not matter however unless the parser pays attention to file endings like json or xml.
   * </p>
   */
  private static final String DEFAULT_NAME = "default-name.txt";

  private String content;

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
