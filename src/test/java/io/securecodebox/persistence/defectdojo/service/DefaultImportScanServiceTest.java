// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.config.Config;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link DefaultImportScanService}
 */
class DefaultImportScanServiceTest {
    private final Config config = new Config(
        "http://localhost",
        "apiKey",
        23
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
    @Disabled("Not implemented yet")
    void importScan_shouldPassImportScanAsEndpoint() {
    }

    @Test
    @Disabled("Not implemented yet")
    void importScan_shouldPassEngagementIdAsEngagement() {
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
