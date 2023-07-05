package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.ScanType;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.models.ScanFile;
import io.securecodebox.persistence.defectdojo.service.DefaultImportScanService.MissingProxyAuthenticationConfig;
import io.securecodebox.persistence.defectdojo.service.ImportScanService.ProxyConfigNames;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests for {@link DefaultImportScanService}
 */
class DefaultImportScanServiceTest {
    private final DefectDojoConfig config = new DefectDojoConfig(
        "http://localhost",
        "apiKey",
        "username",
        23,
        42L
    );
    private final DefaultImportScanService sut = new DefaultImportScanService(config);

    @Test
    void constructorShouldThrowExceptionOnNullConfig() {
        assertThrows(NullPointerException.class, () -> {
            new DefaultImportScanService(null);
        });
    }

    @Test
    void createDefectDojoAuthorizationHeaders_apiKeyFromConfigShouldBePresentAsAuthHeader() {
        final var authorizationHeaders = sut.createDefectDojoAuthorizationHeaders();

        assertAll(
            () -> assertThat(authorizationHeaders.size(), is(1)),
            () -> assertThat(authorizationHeaders.get(HttpHeaders.AUTHORIZATION).get(0), is("Token apiKey"))
        );
    }

    @Test
    void generateApiUrl() {
        assertThat(sut.generateApiUrl("foo"), is("http://localhost/api/v2/foo/"));
    }

    @Test
    @Disabled("Fails due to network side effect")
    void importScan_withoutOptionsShouldPassEmptyMap() {
        final var spiedSut = spy(sut);

        sut.importScan(new ScanFile("content"),42L, 43L, "1.1.2023", ScanType.AUDIT_JS_SCAN, 23L);

        verify(spiedSut, times(1))
            .importScan(new ScanFile("content"),42L, 43L, "1.1.2023", ScanType.AUDIT_JS_SCAN, 23L, new HashMap<>());
    }

    @Test
    @Disabled("Not implemented yet")
    void importScan_shouldPassImportScanAsEndpoint() {
    }

    @Test
    @Disabled("Not implemented yet")
    void importScan_shouldPassEngagementIdAsEngagement() {
    }

    @Test
    @Disabled("Not implemented yet")
    void reimportScan_withoutOptionsShouldPassEmptyMap() {
    }

    @Test
    @Disabled("Not implemented yet")
    void reimportScan_shouldPassReimportScanAsEndpoint() {
    }

    @Test
    @Disabled("Not implemented yet")
    void reimportScan_shouldPassEngagementIdAsTest() {
    }
}
