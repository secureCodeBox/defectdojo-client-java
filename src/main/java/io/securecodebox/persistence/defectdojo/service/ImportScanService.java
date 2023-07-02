// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.securecodebox.persistence.defectdojo.ScanType;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.exceptions.DefectDojoPersistenceException;
import io.securecodebox.persistence.defectdojo.models.ScanFile;
import lombok.Data;
import lombok.NonNull;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.impl.client.BasicCredentialsProvider;
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

public class ImportScanService {

    private final String defectDojoUrl;
    private final String defectDojoApiKey;

    /**
     * Dedicated constructor.
     *
     * @param config not {@code null}
     */
    public ImportScanService(final @NonNull DefectDojoConfig config) {
        super();
        this.defectDojoUrl = config.getUrl();
        this.defectDojoApiKey = config.getApiKey();
    }

    /**
     * @return The DefectDojo Authentication Header
     */
    private HttpHeaders createDefectDojoAuthorizationHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "Token " + defectDojoApiKey);
        return headers;
    }

    protected RestTemplate createRestTemplate() {
        if (System.getProperty("http.proxyUser") != null && System.getProperty("http.proxyPassword") != null) {
            // Configuring Proxy Authentication explicitly as it isn't done by default for spring rest templates :(
            final var credentials = new BasicCredentialsProvider();
            credentials.setCredentials(
                    new AuthScope(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort"))),
                    new UsernamePasswordCredentials(System.getProperty("http.proxyUser"), System.getProperty("http.proxyPassword"))
            );

            final var clientBuilder = HttpClientBuilder.create();

            clientBuilder.useSystemProperties();
            clientBuilder.setProxy(new HttpHost(System.getProperty("http.proxyHost"), Integer.parseInt(System.getProperty("http.proxyPort"))));
            clientBuilder.setDefaultCredentialsProvider(credentials);
            clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

            final var factory = new HttpComponentsClientHttpRequestFactory();
            factory.setHttpClient(clientBuilder.build());
            return new RestTemplate(factory);
        } else {
            return new RestTemplate();
        }
    }

    /**
     * Before version 1.5.4. testName (in DefectDojo _test_type_) must be defectDojoScanName, afterwards, you can have somethings else
     */
    protected ImportScanResponse createFindings(ScanFile scanFile, String endpoint, long lead, String currentDate, ScanType scanType, long testType, MultiValueMap<String, Object> options) {
        var restTemplate = this.createRestTemplate();
        HttpHeaders headers = createDefectDojoAuthorizationHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        restTemplate.setMessageConverters(List.of(
                new FormHttpMessageConverter(),
                new ResourceHttpMessageConverter(),
                new MappingJackson2HttpMessageConverter())
        );

        final var body = new LinkedMultiValueMap<String, Object>();

        body.add("lead", Long.toString(lead));
        body.add("scan_date", currentDate);
        body.add("scan_type", scanType.getTestType());
        body.add("close_old_findings", "true");
        body.add("skip_duplicates", "false");
        body.add("test_type", String.valueOf(testType));

        for (final var theKey : options.keySet()) {
            body.remove(theKey);
        }

        body.addAll(options);

        try {
            ByteArrayResource contentsAsResource = new ByteArrayResource(scanFile.getContent().getBytes(StandardCharsets.UTF_8)) {
                @Override
                public String getFilename() {
                    return scanFile.getName();
                }
            };

            body.add("file", contentsAsResource);

            final var payload = new HttpEntity<>(body, headers);

            return restTemplate.exchange(defectDojoUrl + "/api/v2/" + endpoint + "/", HttpMethod.POST, payload, ImportScanResponse.class).getBody();
        } catch (HttpClientErrorException e) {
            throw new DefectDojoPersistenceException("Failed to attach findings to engagement.");
        }
    }

    public ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType) {
        var additionalValues = new LinkedMultiValueMap<String, Object>();
        additionalValues.add("engagement", Long.toString(engagementId)); // FIXME Seems to be duplicated.

        return this.importScan(scanFile, engagementId, lead, currentDate, scanType, testType, additionalValues);
    }

    public ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType, LinkedMultiValueMap<String, Object> additionalValues) {
        additionalValues.add("engagement", Long.toString(engagementId));

        return this.createFindings(scanFile, "import-scan", lead, currentDate, scanType, testType, additionalValues);
    }

    public ImportScanResponse reimportScan(ScanFile scanFile, long testId, long lead, String currentDate, ScanType scanType, long testType) {
        var additionalValues = new LinkedMultiValueMap<String, Object>();
        additionalValues.add("test", Long.toString(testId)); // FIXME Seems to be duplicated.

        return this.reimportScan(scanFile, testId, lead, currentDate, scanType, testType, additionalValues);
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
