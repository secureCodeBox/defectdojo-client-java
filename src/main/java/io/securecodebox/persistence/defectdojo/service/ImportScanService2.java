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
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.http.Foo;
import io.securecodebox.persistence.defectdojo.model.ScanFile;
import lombok.Data;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
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
 */
public class ImportScanService2 {

    private final Config config;
    @Deprecated
    protected String defectDojoUrl;
    @Deprecated
    protected String defectDojoApiKey;

    public ImportScanService2(Config config) {
        super();
        this.config = config;
        this.defectDojoUrl = config.getUrl();
        this.defectDojoApiKey = config.getApiKey();
    }

    /**
     * @return The DefectDojo Authentication Header
     */
    private HttpHeaders getDefectDojoAuthorizationHeaders() {
        return new Foo(config).getDefectDojoAuthorizationHeaders();
    }

    protected RestTemplate setupRestTemplate() {
        RestTemplate restTemplate;

        if (System.getProperty("http.proxyUser") != null && System.getProperty("http.proxyPassword") != null) {
            // Configuring Proxy Authentication explicitly as it isn't done by default for spring rest templates :(
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(
                    new AuthScope(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort"))),
                    new UsernamePasswordCredentials(System.getProperty("http.proxyUser"), System.getProperty("http.proxyPassword"))
            );
            HttpClientBuilder clientBuilder = HttpClientBuilder.create();

            clientBuilder.useSystemProperties();
            clientBuilder.setProxy(new HttpHost(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort"))));
            clientBuilder.setDefaultCredentialsProvider(credsProvider);
            clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

            CloseableHttpClient client = clientBuilder.build();

            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(client);
            restTemplate = new RestTemplate(factory);
        } else {
            restTemplate = new RestTemplate();
        }

        return restTemplate;
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

            return restTemplate.exchange(defectDojoUrl + "/api/v2/" + endpoint + "/", HttpMethod.POST, payload, ImportScanResponse.class).getBody();
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
