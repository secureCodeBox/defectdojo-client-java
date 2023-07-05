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
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

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
    static ImportScanService createDefault(final DefectDojoConfig config) {
        return new DefaultImportScanService(config);
    }

    default ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType) {
        return this.importScan(scanFile, engagementId, lead, currentDate, scanType, testType, new HashMap<>());
    }

    ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType, Map<String, String> options);

    default ImportScanResponse reimportScan(ScanFile scanFile, long testId, long lead, String currentDate, ScanType scanType, long testType) {
        return this.reimportScan(scanFile, testId, lead, currentDate, scanType, testType, new HashMap<>());
    }

    ImportScanResponse reimportScan(ScanFile scanFile, long testId, long lead, String currentDate, ScanType scanType, long testType, Map<String, String> options);

    @Data
    class ImportScanResponse {
        @JsonProperty
        protected Boolean verified;

        @JsonProperty
        protected Boolean active;

        @JsonProperty("test")
        protected long testId;
    }

    /**
     * These properties can be configured by passing them to the running Java process w/ flag {@literal -D}
     * <p>
     * Example: {@literal java -Dhttp.proxyHost=... -D... -jar ...}
     * </p>
     * <p>
     * <strong>Important</strong>: All four parameters are mandatory. You must set them all
     * or none of them!
     * </p>
     */
    enum ProxyConfigNames {
        HTTP_PROXY_HOST("http.proxyHost"),
        HTTP_PROXY_PORT("http.proxyPort"),
        HTTP_PROXY_USER("http.proxyUser"),
        HTTP_PROXY_PASSWORD("http.proxyPassword");

        @Getter
        private final String literat;

        ProxyConfigNames(String literat) {
            this.literat = literat;
        }
    }
}
