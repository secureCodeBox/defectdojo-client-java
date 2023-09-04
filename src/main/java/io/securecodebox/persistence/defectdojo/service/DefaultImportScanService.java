// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.ScanType;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.http.ProxyConfig;
import io.securecodebox.persistence.defectdojo.model.ScanFile;
import lombok.Getter;
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
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/*
 * https://defectdojo.security.iteratec.dev/api/v2/oa3/swagger-ui/#operations-tag-import-scan
 */
class DefaultImportScanService implements ImportScanService {
    private static final List<HttpMessageConverter<?>> HTTP_MESSAGE_CONVERTERS = List.of(
            new FormHttpMessageConverter(),
            new ResourceHttpMessageConverter(),
            new MappingJackson2HttpMessageConverter());
    @Getter
    private final String defectDojoUrl;
    @Getter
    private final String defectDojoApiKey;
    private final ProxyConfig proxyConfig;

    /**
     * Dedicated constructor.
     *
     * @param config      not {@code null}
     * @param proxyConfig not {@code null}
     */
    DefaultImportScanService(final @NonNull Config config, @NonNull ProxyConfig proxyConfig) {
        super();
        this.defectDojoUrl = config.getUrl();
        this.defectDojoApiKey = config.getApiKey();
        this.proxyConfig = proxyConfig;
    }

    @Override
    public ImportScanResponse importScan(ScanFile scanFile, long engagementId, long lead, String currentDate, ScanType scanType, long testType, Map<String, String> options) {
        options.put("engagement", Long.toString(engagementId));

        return this.createFindings(scanFile, "import-scan", lead, currentDate, scanType, testType, options);
    }

    @Override
    public ImportScanResponse reimportScan(ScanFile scanFile, long testId, long lead, String currentDate, ScanType scanType, long testType, Map<String, String> options) {
        options.put("test", Long.toString(testId));

        return this.createFindings(scanFile, "reimport-scan", lead, currentDate, scanType, testType, options);
    }

    /*
     * Before version 1.5.4. testName (in DefectDojo _test_type_) must be defectDojoScanName, afterward, you can have something else.
     */
    private ImportScanResponse createFindings(ScanFile scanFile, String endpoint, long lead, String currentDate, ScanType scanType, long testType, Map<String, String> options) {
        final var headers = createDefectDojoAuthorizationHeaders();
        // We use multipart because we send two "parts" in the request body:
        // 1. generic info as key=value&key=value...
        // 2. the raw scan result as file
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // FIXME: #36 Why do we use a multi value map here? Do we need multiple values for any given key?
        final var body = new LinkedMultiValueMap<String, Object>();

        body.add("lead", Long.toString(lead));
        body.add("scan_date", currentDate);
        body.add("scan_type", scanType.getTestType());
        body.add("close_old_findings", "true");
        body.add("skip_duplicates", "false");
        body.add("test_type", String.valueOf(testType));

        // Avoid duplicate entries:
        for (final var optionName : options.keySet()) {
            body.remove(optionName);
        }

        // FIXME: #36 Workaround due to type incompatibility of MultiValueMap<String, String> and MultiValueMap<String, Object>.
        for (final var option : options.entrySet()) {
            body.add(option.getKey(), option.getValue());
        }

        try {
            // scanFile is the raw result from lurker.
            final var contentsAsResource = new ByteArrayResource(scanFile.getContent().getBytes(StandardCharsets.UTF_8)) {
                @Override
                public String getFilename() {
                    return scanFile.getName();
                }
            };

            // We send the whole file content, so DefectDojo can parse the finding by itself.
            body.add("file", contentsAsResource);

            final var payload = new HttpEntity<MultiValueMap<String, Object>>(body, headers);
            return exchangeRequest(endpoint, payload);
        } catch (HttpClientErrorException e) {
            throw new PersistenceException("Failed to attach findings to engagement.");
        }
    }

    ImportScanResponse exchangeRequest(String endpoint, HttpEntity<?> payload) {
        final var restTemplate = this.createRestTemplate();
        return restTemplate.exchange(
                        generateApiUrl(endpoint),
                        HttpMethod.POST,
                        payload,
                        ImportScanResponse.class)
                .getBody();
    }

    String generateApiUrl(final String endpoint) {
        return String.format("%s/api/v2/%s/", getDefectDojoUrl(), endpoint);
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

        template.setMessageConverters(HTTP_MESSAGE_CONVERTERS);

        return template;
    }

    boolean shouldConfigureProxySettings() {
        return proxyConfig.isComplete();
    }

    /**
     * Configuring proxy authentication explicitly
     *
     * <p>
     * This isn't done by default for spring rest templates.This method expects these four system properties (Java flag
     * {@literal -DpropertyName}) to be set:
     * </p>
     * <ul>
     *     <li>http.proxyUser</li>
     *     <li>http.proxyPassword</li>
     *     <li>http.proxyHost</li>
     *     <li>http.proxyPort</li>
     * </ul>
     *
     * @return never {@code null}
     */
    ClientHttpRequestFactory createRequestFactoryWithProxyAuthConfig() {
        final var credentials = new BasicCredentialsProvider();
        credentials.setCredentials(
                new AuthScope(proxyConfig.getHost(), proxyConfig.getPort()),
                new UsernamePasswordCredentials(
                        proxyConfig.getUser(),
                        proxyConfig.getPassword())
        );

        final var clientBuilder = HttpClientBuilder.create();
        clientBuilder.useSystemProperties();
        clientBuilder.setProxy(new HttpHost(proxyConfig.getHost(), proxyConfig.getPort()));
        clientBuilder.setDefaultCredentialsProvider(credentials);
        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());

        final var factory = new HttpComponentsClientHttpRequestFactory();
        factory.setHttpClient(clientBuilder.build());
        return factory;
    }
}
