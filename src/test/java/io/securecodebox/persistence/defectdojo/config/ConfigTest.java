// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.config;

import io.securecodebox.persistence.defectdojo.exception.ConfigException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import uk.org.webcompere.systemstubs.environment.EnvironmentVariables;
import uk.org.webcompere.systemstubs.jupiter.SystemStub;
import uk.org.webcompere.systemstubs.jupiter.SystemStubsExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link Config}
 */
@ExtendWith(SystemStubsExtension.class)
class ConfigTest {

    @SystemStub
    private EnvironmentVariables environmentVariables;

    @Test
    void constructor_urlMustNotBeNull() {
        final var thrown = assertThrows(NullPointerException.class, () -> {
            new Config(null, "apiKey", 1, 1);
        });

        assertThat(thrown.getMessage(), startsWith("url "));
    }

    @Test
    void constructor_apiKeyMustNotBeNull() {
        final var thrown = assertThrows(NullPointerException.class, () -> {
            new Config("url", null, 1, 1);
        });

        assertThat(thrown.getMessage(), startsWith("apiKey "));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -2, -23, -42, Integer.MIN_VALUE})
    void constructor_refetchWaitSecondsMustNotBeLessThanZero(final int number) {
        final var thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Config("url", "apiKey", 1, number);
        });

        assertThat(thrown.getMessage(), startsWith("refetchWaitSeconds "));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -23, -42, Integer.MIN_VALUE})
    void constructor_maxPageCountForGetsMustNotBeLessThanOne(final int number) {
        final var thrown = assertThrows(IllegalArgumentException.class, () -> {
            new Config("url", "apiKey", number, 60 );
        });

        assertThat(thrown.getMessage(), startsWith("maxPageCountForGets "));
    }

    @Test
    void fromEnv() {
        environmentVariables
            .set("DEFECTDOJO_URL", "url")
            .set("DEFECTDOJO_APIKEY", "apikey")
            .set("DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS", "23")
            .set("DEFECTDOJO_REFETCH_WAIT_SECONDS", "60");

        final var sut = Config.fromEnv();

        assertAll(
            () -> assertThat(sut.getUrl(), is("url")),
            () -> assertThat(sut.getApiKey(), is("apikey")),
            () -> assertThat(sut.getMaxPageCountForGets(), is(23)),
            () -> assertThat(sut.getRefetchWaitSeconds(), is(60))
        );
    }

    @Test
    void fromEnv_throwsExceptionIfNoUrlSet() {
        environmentVariables
            .set("DEFECTDOJO_APIKEY", "apikey");

        final var thrown = assertThrows(ConfigException.class, Config::fromEnv);

        assertThat(thrown.getMessage(), is("Missing environment variable 'DEFECTDOJO_URL'!"));
    }

    @Test
    void fromEnv_throwsExceptionIfNoApiKeySet() {
        environmentVariables
            .set("DEFECTDOJO_URL", "url");

        final var thrown = assertThrows(ConfigException.class, Config::fromEnv);

        assertThat(thrown.getMessage(), is("Missing environment variable 'DEFECTDOJO_APIKEY'!"));
    }

    @Test
    void fromEnv_usesDefaultIfNoRefetchWaitSecondsSet() {
        environmentVariables
            .set("DEFECTDOJO_URL", "url")
            .set("DEFECTDOJO_APIKEY", "apikey")
            .set("DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS", 1);

        final var sut = Config.fromEnv();
        assertThat(sut.getRefetchWaitSeconds(), is(Config.DEFAULT_REFETCH_WAIT_SECONDS));
    }

    @Test
    void fromEnv_throwsExceptionIfRefetchWaitSecondsIsNotParseableToInteger() {
        environmentVariables
            .set("DEFECTDOJO_URL", "url")
            .set("DEFECTDOJO_APIKEY", "apikey")
            .set("DEFECTDOJO_REFETCH_WAIT_SECONDS", "foo");

        final var thrown = assertThrows(ConfigException.class, Config::fromEnv);

        assertThat(thrown.getMessage(), is("Given value for environment variable 'DEFECTDOJO_REFETCH_WAIT_SECONDS' is not a valid number! Given was 'foo'."));
    }

    @Test
    void fromEnv_usesDefaultIfNoMaxPageCountForGetSet() {
        environmentVariables
            .set("DEFECTDOJO_URL", "url")
            .set("DEFECTDOJO_APIKEY", "apikey");

        final var sut = Config.fromEnv();
        assertThat(sut.getMaxPageCountForGets(), is(Config.DEFAULT_MAX_PAGE_COUNT_FOR_GETS));
    }

    @Test
    void fromEnv_throwsExceptionIfMaxPageCountForGetIsNotParseableToInteger() {
        environmentVariables
            .set("DEFECTDOJO_URL", "url")
            .set("DEFECTDOJO_APIKEY", "apikey")
            .set("DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS", "foo");

        final var thrown = assertThrows(ConfigException.class, Config::fromEnv);

        assertThat(thrown.getMessage(), is("Given value for environment variable 'DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS' is not a valid number! Given was 'foo'."));
    }
}
