// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.securecodebox.persistence.defectdojo.ScanType;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import io.securecodebox.persistence.defectdojo.http.ProxyConfig;
import io.securecodebox.persistence.defectdojo.http.ProxyConfigFactory;
import io.securecodebox.persistence.defectdojo.model.ScanFile;
import lombok.Data;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Service to re/import findings into DefectDojo
 */
public interface ImportScanService {
  /**
   * Factory method to create new instance of service default implementation
   *
   * @param clientConfig must not be {@code null}
   * @return never {@code null}
   */
  static ImportScanService createDefault(final ClientConfig clientConfig) {
    return createDefault(clientConfig, new ProxyConfigFactory().create());
  }

  /**
   * Factory method to create new instance of service default implementation
   *
   * @param clientConfig      must not be {@code null}
   * @param proxyConfig must not be {@code null}
   * @return never {@code null}
   */
  static ImportScanService createDefault(@NonNull final ClientConfig clientConfig, @NonNull final ProxyConfig proxyConfig) {
    return new DefaultImportScanService(clientConfig, proxyConfig);
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
}
