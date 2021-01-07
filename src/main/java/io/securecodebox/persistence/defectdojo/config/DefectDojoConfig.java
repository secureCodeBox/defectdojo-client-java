package io.securecodebox.persistence.defectdojo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class DefectDojoConfig {
    @Getter
    private final String url;

    @Getter
    private final String apiKey;

    @Getter
    private final String username;

    public static DefectDojoConfig fromEnv(){
        String url = System.getenv("DEFECTDOJO_URL");
        String username = System.getenv("DEFECTDOJO_USERNAME");
        String apiKey = System.getenv("DEFECTDOJO_APIKEY");
        return new DefectDojoConfig(url, apiKey, username);
    }
}
