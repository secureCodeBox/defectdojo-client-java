package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link UserProfile}
 */
class UserProfileTest {
  @Test
  void equalsAndHashCode() {
    EqualsVerifier.forClass(UserProfile.class)
      .suppress(Warning.NONFINAL_FIELDS)
      .verify();
  }

  @Test
  void equalsQueryString_alwaysTrue() {
    final var sut = UserProfile.builder()
      .build();

    assertThat(sut.equalsQueryString(null), is(true));
    assertThat(sut.equalsQueryString(Collections.emptyMap()), is(true));
  }
}
