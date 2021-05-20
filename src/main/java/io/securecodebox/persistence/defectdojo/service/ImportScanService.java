/*
 *  secureCodeBox (SCB)
 *  Copyright 2021 iteratec GmbH
 *  https://www.iteratec.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.securecodebox.persistence.defectdojo.ScanType;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.exceptions.DefectDojoPersistenceException;
import io.securecodebox.persistence.defectdojo.models.ScanFile;
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

public class ImportScanService {

  protected String defectDojoUrl;
  protected String defectDojoApiKey;

  public ImportScanService(DefectDojoConfig config){
    this.defectDojoUrl = config.getUrl();
    this.defectDojoApiKey = config.getApiKey();
  }

  /**
   * @return The DefectDojo Authentication Header
   */
  private HttpHeaders getDefectDojoAuthorizationHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.set("Authorization", "Token " + defectDojoApiKey);
    return headers;
  }

  /**
   * Before version 1.5.4. testName (in DefectDojo _test_type_) must be defectDojoScanName, afterwards, you can have somethings else
   */
  protected ImportScanResponse createFindings(ScanFile scanFile, String endpoint, long lead, String currentDate, ScanType scanType, long testType, MultiValueMap<String, Object> options) {
    RestTemplate restTemplate = new RestTemplate();
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

      return restTemplate.exchange(defectDojoUrl + "/api/v2/" + endpoint + "/", HttpMethod.POST, payload, ImportScanResponse.class).getBody();
    } catch (HttpClientErrorException e) {
      throw new DefectDojoPersistenceException("Failed to attach findings to engagement.");
    }
  }

  public ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType) {
    var additionalValues = new LinkedMultiValueMap<String, Object>();
    additionalValues.add("engagement", Long.toString(engagementId));

    return this.createFindings(scanFile, "import-scan", lead, currentDate, scanType, testType, additionalValues);
  }

  public ImportScanResponse reimportScan(ScanFile scanFile, long testId, long lead, String currentDate, ScanType scanType, long testType) {
    var additionalValues = new LinkedMultiValueMap<String, Object>();
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
