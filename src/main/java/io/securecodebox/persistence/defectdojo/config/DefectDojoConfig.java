package io.securecodebox.persistence.defectdojo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DefectDojoConfig {
    @Getter
    private final String url;

    @Getter
    private final String apiKey;

    static DefectDojoConfig fromEnv(){
        String url = System.getenv("DEFECTDOJO_URL");
        String username = System.getenv("DEFECTDOJO_USERNMAE");
        return new DefectDojoConfig(url, username);
    }
}
