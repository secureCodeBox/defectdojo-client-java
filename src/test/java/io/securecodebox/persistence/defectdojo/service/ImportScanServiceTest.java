package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ImportScanService}
 */
class ImportScanServiceTest {
    private final DefectDojoConfig config = new DefectDojoConfig(
            "url",
            "apiKey",
            "username",
            23,
            42L
    );
    private final ImportScanService sut = new ImportScanService(config);

    @Test
    void constructorShouldThrowExceptionOnNullConfig() {
        assertThrows(NullPointerException.class, () -> {
            new ImportScanService(null);
        });
    }
}
