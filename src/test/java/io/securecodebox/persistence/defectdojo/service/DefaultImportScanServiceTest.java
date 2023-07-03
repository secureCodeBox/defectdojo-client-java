package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Tests for {@link DefaultImportScanService}
 */
class DefaultImportScanServiceTest {
    private final DefectDojoConfig config = new DefectDojoConfig(
            "url",
            "apiKey",
            "username",
            23,
            42L
    );
    private final DefaultImportScanService sut = new DefaultImportScanService(config);

    @Test
    void constructorShouldThrowExceptionOnNullConfig() {
        assertThrows(NullPointerException.class, () -> {
            new DefaultImportScanService(null);
        });
    }

    @Test
    void createDefectDojoAuthorizationHeaders_apiKeyFromConfigShouldBePresentAsAuthHEader() {
        final var authorizationHeaders = sut.createDefectDojoAuthorizationHeaders();
        assertAll(
                () -> assertEquals(1, authorizationHeaders.size(), "Expected is exactly one authorization header!"),
                () -> assertEquals("Token apiKey", authorizationHeaders.get(HttpHeaders.AUTHORIZATION).get(0))
        );
    }
}
