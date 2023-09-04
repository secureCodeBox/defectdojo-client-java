// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.http;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link MissingProxyConfigValue}
 */
class MissingProxyConfigValueTest {
    @Test
    void rendersMessageFromProxyConfigName() {
        final var sut = new MissingProxyConfigValue(ProxyConfigNames.HTTP_PROXY_HOST);

        assertThat(sut.getMessage(), is("Expected system property 'http.proxyHost' not set!"));
    }
}
