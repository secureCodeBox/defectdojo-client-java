package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ImportScanService}.
 */
class ImportScanServiceTest {

    @Test
    void createDefault_throwsExceptionIfNullPassedIn() {
        assertThrows(NullPointerException.class, () -> {
            ImportScanService.createDefault(null);
        });
    }
    
    @Test
    void createDefault_passesConfig() {
        final var config = new DefectDojoConfig(
            "url",
            "apiKey",
            "username",
            23,
            42L
        );

        final var sut = (DefaultImportScanService) ImportScanService.createDefault(config);

        assertAll(
            () -> assertThat(sut.getDefectDojoUrl(), is(config.getUrl())),
            () -> assertThat(sut.getDefectDojoApiKey(), is(config.getApiKey())));
    }
}
