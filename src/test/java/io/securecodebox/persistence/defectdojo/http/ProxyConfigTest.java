// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.http;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ProxyConfig}
 */
class ProxyConfigTest {
    @Test
    void equalsAndHashCode() {
        EqualsVerifier.forClass(ProxyConfig.class).verify();
    }

    @Test
    void builderCreatesDefault() {
        final var sut = ProxyConfig.builder().build();

        assertAll(
            () -> assertThat(sut.getUser(), is(emptyString())),
            () -> assertThat(sut.getPassword(), is(emptyString())),
            () -> assertThat(sut.getHost(), is(emptyString())),
            () -> assertThat(sut.getPort(), is(0))
        );
    }

    @Test
    void buildersDefaultIsEqualToNullObject() {
        assertThat(ProxyConfig.builder().build(), is(ProxyConfig.NULL));
    }

    @Test
    void isComplete_falseForNullObject() {
        assertThat(ProxyConfig.NULL.isComplete(), is(false));
    }

    @Test
    void isComplete_falseForDefault() {
        assertThat(ProxyConfig.builder().build().isComplete(), is(false));
    }

    @ParameterizedTest
    @MethodSource("incompleteConfigs")
    void isComplete_falseUnlessAllFieldsAreSet(final ProxyConfig sut) {

    }

    private static Stream<Arguments> incompleteConfigs() {
        return Stream.of(
            // Only one is set:
            Arguments.of(ProxyConfig.builder().user("user").build()),
            Arguments.of(ProxyConfig.builder().password("pw").build()),
            Arguments.of(ProxyConfig.builder().host("host").build()),
            Arguments.of(ProxyConfig.builder().port(42).build()),
            // All but one is set:
            Arguments.of(ProxyConfig.builder().password("pwd").host("host").port(42).build()),
            Arguments.of(ProxyConfig.builder().user("user").host("host").port(42).build()),
            Arguments.of(ProxyConfig.builder().user("user").password("pwd").port(42).build()),
            Arguments.of(ProxyConfig.builder().user("user").password("pwd").host("host").build())
        );
    }

    @Test
    void isComplete_trueIfAllFieldsAreNonDefaults() {
        final var sut = ProxyConfig.builder()
            .user("user")
            .password("pw")
            .host("host")
            .port(42)
            .build();

        assertThat(sut.isComplete(), is(true));
    }
}
