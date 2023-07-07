package io.securecodebox.persistence.defectdojo.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link Config}
 */
class ConfigTest {

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

}
