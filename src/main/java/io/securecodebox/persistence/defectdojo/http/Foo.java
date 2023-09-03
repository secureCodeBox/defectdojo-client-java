package io.securecodebox.persistence.defectdojo.http;

import io.securecodebox.persistence.defectdojo.config.Config;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Placeholder to move duplicated code, will be named better later
 */
public final class Foo {
    private final Config config;

    public Foo(@NonNull final Config config) {
        super();
        this.config = config;
    }

    public HttpHeaders getDefectDojoAuthorizationHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Token " + this.config.getApiKey());

        String username = System.getProperty("http.proxyUser", "");
        String password = System.getProperty("http.proxyPassword", "");

        if (!username.isEmpty() || !password.isEmpty()) {
            System.out.println("Setting Proxy Auth Header...");
            headers.set(HttpHeaders.PROXY_AUTHORIZATION, "Basic " + Base64.getEncoder().encodeToString((username + ':' + password).getBytes(StandardCharsets.UTF_8)));
        }

        return headers;
    }
}
