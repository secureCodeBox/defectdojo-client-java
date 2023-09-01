// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import lombok.Data;

@Data
public class ScanFile {
    /**
     * A default name must be set
     * <p>
     * It does not matter however unless the parser pays attention to file endings like json or xml.
     * </p>
     */
    static final String DEFAULT_NAME = "default-name.txt";

    String content;

    String name;

    public ScanFile(String content) {
        this(content, DEFAULT_NAME);
    }

    public ScanFile(String content, String name) {
        super();
        this.content = content;
        this.name = name;
    }

}
