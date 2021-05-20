package io.securecodebox.persistence.defectdojo.models;

import lombok.Data;

@Data
public class ScanFile {

    String content;

    // a default name must be set, it does not matter however
    // unless the parser pays attention to file endings like json or xml
    String name = "default-name.txt";

}
