package io.securecodebox.persistence.defectdojo.config;

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
        final var thrown = assertThrows(NullPointerException.class, () ->{
            new Config(null, "apiKey", "username", 1,null);
        });

        assertThat(thrown.getMessage(), startsWith("url "));
    }

    @Test
    void constructor_apiKeyMustNotBeNull() {
        final var thrown = assertThrows(NullPointerException.class, () ->{
            new Config("url", null, "username", 1,null);
        });

        assertThat(thrown.getMessage(), startsWith("apiKey "));
    }

    @Test
    void constructor_usernameMustNotBeNull() {
        final var thrown = assertThrows(NullPointerException.class, () ->{
            new Config("url", "apiKey", null, 1,null);
        });

        assertThat(thrown.getMessage(), startsWith("username "));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -2, -23, -42, Integer.MIN_VALUE})
    void constructor_maxPageCountForGetsMustNotBeLessThanOne(final int number) {
        final var thrown = assertThrows(IllegalArgumentException.class, () ->{
            new Config("url", "apiKey", "username", number,null);
        });

        assertThat(thrown.getMessage(), startsWith("maxPageCountForGets "));
    }

    @Test
    void fromEnv() {
        environmentVariables.set("DEFECTDOJO_URL", "url")
            .set("DEFECTDOJO_USERNAME", "username")
            .set("DEFECTDOJO_APIKEY", "apikey")
            .set("DEFECTDOJO_USER_ID", "42")
            .set("DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS", "23");

        final var sut = Config.fromEnv();

        assertAll(
            () -> assertThat(sut.getUrl(), is("url")),
            () -> assertThat(sut.getUsername(), is("username")),
            () -> assertThat(sut.getApiKey(), is("apikey")),
            () -> assertThat(sut.getUserId(), is(42L)),
            () -> assertThat(sut.getMaxPageCountForGets(), is(23))
        );
    }
}
