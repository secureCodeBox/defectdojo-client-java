// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.ScanType;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.ScanFile;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ImportScanService}.
 */
class ImportScanServiceTest {

    private final ImportScanServiceStub sut = new ImportScanServiceStub();

    @Test
    void createDefault_throwsExceptionIfNullPassedIn() {
        assertThrows(NullPointerException.class, () -> {
            ImportScanService.createDefault(null);
        });
    }

    @Test
    void createDefault_passesConfig() {
        final var config = new Config(
            "url",
            "apiKey",
            23
        );

        final var sut = (DefaultImportScanService) ImportScanService.createDefault(config);

        assertAll(
            () -> assertThat(sut.getDefectDojoUrl(), is(config.getUrl())),
            () -> assertThat(sut.getDefectDojoApiKey(), is(config.getApiKey())));
    }

    @Test
    void importScan_withoutOptionsShouldPassEmptyMap() {
        assertThat(sut.getOptions(), is(nullValue()));

        sut.importScan(new ScanFile("content"), 42L, 43L, "1.1.2023", ScanType.AUDIT_JS_SCAN, 23L);

        assertThat(sut.getOptions(), equalTo(Collections.EMPTY_MAP));
    }

    @Test
    void importScan_withoutOptionsShouldPassModifiableMap() {
        assertThat(sut.getOptions(), is(nullValue()));

        sut.importScan(new ScanFile("content"), 42L, 43L, "1.1.2023", ScanType.AUDIT_JS_SCAN, 23L);
        sut.getOptions().put("foo", "bar");

        assertThat(sut.getOptions(), hasEntry("foo", "bar"));
    }

    @Test
    void reimportScan_withoutOptionsShouldPassEmptyMap() {
        assertThat(sut.getOptions(), is(nullValue()));

        sut.reimportScan(new ScanFile("content"), 42L, 43L, "1.1.2023", ScanType.AUDIT_JS_SCAN, 23L);

        assertThat(sut.getOptions(), equalTo(Collections.EMPTY_MAP));
    }

    @Test
    void reimportScan_withoutOptionsShouldPassModifiableMap() {
        assertThat(sut.getOptions(), is(nullValue()));

        sut.reimportScan(new ScanFile("content"), 42L, 43L, "1.1.2023", ScanType.AUDIT_JS_SCAN, 23L);
        sut.getOptions().put("foo", "bar");

        assertThat(sut.getOptions(), hasEntry("foo", "bar"));
    }

    private static final class ImportScanServiceStub implements ImportScanService {

        @Getter
        private Map<String, String> options;

        @Override
        public ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType, Map<String, String> options) {
            this.options = options;
            return null;
        }

        @Override
        public ImportScanResponse reimportScan(ScanFile scanFile, long testId, long lead, String currentDate, ScanType scanType, long testType, Map<String, String> options) {
            this.options = options;
            return null;
        }
    }
}
