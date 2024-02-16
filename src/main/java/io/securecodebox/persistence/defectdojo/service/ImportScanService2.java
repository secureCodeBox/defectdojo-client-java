// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.securecodebox.persistence.defectdojo.ScanType;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.http.AuthHeaderFactory;
import io.securecodebox.persistence.defectdojo.http.Foo;
import io.securecodebox.persistence.defectdojo.http.ProxyConfigFactory;
import io.securecodebox.persistence.defectdojo.model.ScanFile;
import lombok.Data;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Copied the version before I did refactoring for easier compare of duplicated code
 *
 * @deprecated Will be removed when refactoring is finished
 */
@Deprecated(forRemoval = true)
public class ImportScanService2 {

  private final Config config;

  public ImportScanService2(Config config) {
    super();
    this.config = config;
  }

  /**
   * @return The DefectDojo Authentication Header
   */
  private HttpHeaders getDefectDojoAuthorizationHeaders() {
    final var factory = new AuthHeaderFactory(config);
    factory.setProxyConfig(new ProxyConfigFactory().create());
    return factory.generateAuthorizationHeaders();
  }

  protected RestTemplate setupRestTemplate() {
    return new Foo(config, new ProxyConfigFactory().create()).createRestTemplate();
  }

  /**
   * Before version 1.5.4. testName (in DefectDojo _test_type_) must be defectDojoScanName, afterwards, you can have somethings else
   */
  protected ImportScanResponse createFindings(ScanFile scanFile, String endpoint, long lead, String currentDate, ScanType scanType, long testType, MultiValueMap<String, Object> options) {
    var restTemplate = this.setupRestTemplate();
    HttpHeaders headers = getDefectDojoAuthorizationHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    restTemplate.setMessageConverters(List.of(
      new FormHttpMessageConverter(),
      new ResourceHttpMessageConverter(),
      new MappingJackson2HttpMessageConverter())
    );

    MultiValueMap<String, Object> mvn = new LinkedMultiValueMap<>();

    mvn.add("lead", Long.toString(lead));
    mvn.add("scan_date", currentDate);
    mvn.add("scan_type", scanType.getTestType());
    mvn.add("close_old_findings", "true");
    mvn.add("skip_duplicates", "false");
    mvn.add("test_type", String.valueOf(testType));

    for (String theKey : options.keySet()) {
      mvn.remove(theKey);
    }
    mvn.addAll(options);

    try {
      ByteArrayResource contentsAsResource = new ByteArrayResource(scanFile.getContent().getBytes(StandardCharsets.UTF_8)) {
        @Override
        public String getFilename() {
          return scanFile.getName();
        }
      };

      mvn.add("file", contentsAsResource);

      var payload = new HttpEntity<>(mvn, headers);

      return restTemplate.exchange(config.getUrl() + "/api/v2/" + endpoint + "/", HttpMethod.POST, payload, ImportScanResponse.class).getBody();
    } catch (HttpClientErrorException e) {
      throw new PersistenceException("Failed to attach findings to engagement.");
    }
  }


  public ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType) {
    var additionalValues = new LinkedMultiValueMap<String, Object>();
    additionalValues.add("engagement", Long.toString(engagementId));

    return this.importScan(scanFile, engagementId, lead, currentDate, scanType, testType, additionalValues);
  }

  public ImportScanResponse reimportScan(ScanFile scanFile, long testId, long lead, String currentDate, ScanType scanType, long testType) {
    var additionalValues = new LinkedMultiValueMap<String, Object>();
    additionalValues.add("test", Long.toString(testId));

    return this.reimportScan(scanFile, testId, lead, currentDate, scanType, testType, additionalValues);
  }

  //overloading with optional parameter
  public ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType, LinkedMultiValueMap<String, Object> additionalValues) {
    additionalValues.add("engagement", Long.toString(engagementId));

    return this.createFindings(scanFile, "import-scan", lead, currentDate, scanType, testType, additionalValues);
  }

  public ImportScanResponse reimportScan(ScanFile scanFile, long testId, long lead, String currentDate, ScanType scanType, long testType, LinkedMultiValueMap<String, Object> additionalValues) {
    additionalValues.add("test", Long.toString(testId));

    return this.createFindings(scanFile, "reimport-scan", lead, currentDate, scanType, testType, additionalValues);
  }

  @Data
  public static class ImportScanResponse {
    @JsonProperty
    protected Boolean verified;

    @JsonProperty
    protected Boolean active;

    @JsonProperty("test")
    protected long testId;
  }
}
