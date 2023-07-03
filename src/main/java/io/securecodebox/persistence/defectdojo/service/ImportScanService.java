// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.securecodebox.persistence.defectdojo.ScanType;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.models.ScanFile;
import lombok.Data;
import lombok.NonNull;
import org.springframework.util.MultiValueMap;

/**
 * Service to re/import findings into DefectDojo
 */
public interface ImportScanService {
    /**
     * Factory method to create new instance of service default implementation
     *
     * @param config must not be {@code null}
     * @return never {@code null}
     */
    default ImportScanService createDefault(@NonNull DefectDojoConfig config) {
        return new DefaultImportScanService(config);
    }

    ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType);

    ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType, MultiValueMap<String, String> options);

    ImportScanResponse reimportScan(ScanFile scanFile, long testId, long lead, String currentDate, ScanType scanType, long testType);

    ImportScanResponse reimportScan(ScanFile scanFile, long testId, long lead, String currentDate, ScanType scanType, long testType, MultiValueMap<String, String> options);

    @Data
    class ImportScanResponse {
        @JsonProperty
        protected Boolean verified;

        @JsonProperty
        protected Boolean active;

        @JsonProperty("test")
        protected long testId;
    }
}
