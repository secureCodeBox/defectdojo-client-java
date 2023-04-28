// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.models;

import lombok.Data;

@Data
public class ScanFile {

    String content;

    // a default name must be set, it does not matter however
    // unless the parser pays attention to file endings like json or xml
    String name = "default-name.txt";

    public ScanFile(String content){
        this.content = content;
    }

    public ScanFile(String content, String name){
        this.content = content;
        this.name = name;
    }

}
