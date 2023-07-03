// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.ScanType;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.exceptions.DefectDojoPersistenceException;
import io.securecodebox.persistence.defectdojo.models.ScanFile;
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

final class DefaultImportScanService implements ImportScanService {
    private final String defectDojoUrl;
    private final String defectDojoApiKey;

    /**
     * Dedicated constructor.
     *
     * @param config not {@code null}
     */
    DefaultImportScanService(final @NonNull DefectDojoConfig config) {
        super();
        this.defectDojoUrl = config.getUrl();
        this.defectDojoApiKey = config.getApiKey();
    }

    /**
     * The DefectDojo Authentication Header
     *
     * @return never {@code null}
     */
    HttpHeaders createDefectDojoAuthorizationHeaders() {
        final var authorizationHeader = new HttpHeaders();
        authorizationHeader.set(HttpHeaders.AUTHORIZATION, String.format("Token %s", defectDojoApiKey));
        return authorizationHeader;
    }

    private RestTemplate createRestTemplate() {
        final var template = new RestTemplate();

        if (shouldConfigureProxySettings()) {
            template.setRequestFactory(createRequestFactoryWithProxyAuthConfig());
        }

        return template;
    }

    private static boolean shouldConfigureProxySettings() {
        return System.getProperty("http.proxyUser") != null && System.getProperty("http.proxyPassword") != null;
    }

    private static HttpComponentsClientHttpRequestFactory createRequestFactoryWithProxyAuthConfig() {
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
        return factory;
    }

    /*
     * Before version 1.5.4. testName (in DefectDojo _test_type_) must be defectDojoScanName, afterward, you can have something else.
     */
    private ImportScanResponse createFindings(ScanFile scanFile, String endpoint, long lead, String currentDate, ScanType scanType, long testType, MultiValueMap<String, String> options) {
        final var restTemplate = this.createRestTemplate();
        final var headers = createDefectDojoAuthorizationHeaders();
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

        for (final var optionName : options.keySet()) {
            body.remove(optionName);
        }

        // FIXME: Workaround due to type incompatibility of MultiValueMap<String, String> and MultiValueMap<String, Object>.
        for (final var option : options.entrySet()) {
            body.add(option.getKey(), option.getValue());
        }

        try {
            final var contentsAsResource = new ByteArrayResource(scanFile.getContent().getBytes(StandardCharsets.UTF_8)) {
                @Override
                public String getFilename() {
                    return scanFile.getName();
                }
            };

            // FIXME Why do we add the whole byte array resiurce here as object? Is not simply the file name sufficient here? Then we could use <String, String>
            body.add("file", contentsAsResource);

            // FIXME: We do not define the the type T of the body here!
            final var payload = new HttpEntity<MultiValueMap<String, Object>>(body, headers);

            return restTemplate.exchange(defectDojoUrl + "/api/v2/" + endpoint + "/", HttpMethod.POST, payload, ImportScanResponse.class).getBody();
        } catch (HttpClientErrorException e) {
            throw new DefectDojoPersistenceException("Failed to attach findings to engagement.");
        }
    }

    @Override
    public ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType) {
        final var options = new LinkedMultiValueMap<String, String>();
        options.add("engagement", Long.toString(engagementId)); // FIXME Seems to be duplicated bc it is done again in the overloaded method.

        return this.importScan(scanFile, engagementId, lead, currentDate, scanType, testType, options);
    }

    @Override
    public ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType, MultiValueMap<String, String> options) {
        options.add("engagement", Long.toString(engagementId));

        // FIXME: Why is engagementId hardcoded overwritten with "import-scan"
        return this.createFindings(scanFile, "import-scan", lead, currentDate, scanType, testType, options);
    }

    @Override
    public ImportScanResponse reimportScan(ScanFile scanFile, long testId, long lead, String currentDate, ScanType scanType, long testType) {
        final var options = new LinkedMultiValueMap<String, String>();
        options.add("test", Long.toString(testId)); // FIXME Seems to be duplicated bc it is done again in the overloaded method.

        return this.reimportScan(scanFile, testId, lead, currentDate, scanType, testType, options);
    }

    @Override
    public ImportScanResponse reimportScan(ScanFile scanFile, long testId, long lead, String currentDate, ScanType scanType, long testType, MultiValueMap<String, String> options) {
        options.add("test", Long.toString(testId));

        // FIXME: Why is engagementId hardcoded overwritten with "reimport-scan"
        return this.createFindings(scanFile, "reimport-scan", lead, currentDate, scanType, testType, options);
    }
}
