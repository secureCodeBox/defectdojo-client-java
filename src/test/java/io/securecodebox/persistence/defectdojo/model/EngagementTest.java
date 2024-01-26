package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link Engagement}
 */
class EngagementTest {
  @Test
  void equalsAndHashCode() {
    EqualsVerifier.forClass(Engagement.class)
      .suppress(Warning.NONFINAL_FIELDS)
      .verify();
  }

  @Test
  void equalsQueryString_id() {
    final var sut = Engagement.builder().build();
    assertThat(sut.equalsQueryString(null), is(false));

    final var params = new HashMap<String, Object>();
    assertThat(sut.equalsQueryString(params), is(false));

    params.put("id", 42L);
    assertThat(sut.equalsQueryString(params), is(false));

    sut.setId(23L);
    assertThat(sut.equalsQueryString(params), is(false));

    sut.setId(42L);
    assertThat(sut.equalsQueryString(params), is(true));
  }

  @Test
  void equalsQueryString_name() {
    final var sut = Engagement.builder().build();
    assertThat(sut.equalsQueryString(null), is(false));

    final var params = new HashMap<String, Object>();
    assertThat(sut.equalsQueryString(params), is(false));

    params.put("name", "foobar");
    assertThat(sut.equalsQueryString(params), is(false));

    sut.setName("snafu");
    assertThat(sut.equalsQueryString(params), is(false));

    sut.setName("foobar");
    assertThat(sut.equalsQueryString(params), is(true));
  }
}
