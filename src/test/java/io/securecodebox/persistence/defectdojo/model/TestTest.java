package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests for {@link Test}
 */
class TestTest {
  @Test
  void equalsAndHashCode() {
    EqualsVerifier.forClass(Test.class)
      .suppress(Warning.NONFINAL_FIELDS)
      .verify();
  }

  @Test
  void equalsQueryString_id() {
    final var sut = io.securecodebox.persistence.defectdojo.model.Test.builder()
      .build();
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
  void equalsQueryString_title() {
    final var sut = io.securecodebox.persistence.defectdojo.model.Test.builder()
      .build();
    assertThat(sut.equalsQueryString(null), is(false));

    final var params = new HashMap<String, Object>();
    assertThat(sut.equalsQueryString(params), is(false));

    params.put("title", "foobar");
    assertThat(sut.equalsQueryString(params), is(false));

    sut.setTitle("snafu");
    assertThat(sut.equalsQueryString(params), is(false));

    sut.setTitle("foobar");
    assertThat(sut.equalsQueryString(params), is(true));
  }

  @Test
  void equalsQueryString_engagement() {
    final var sut = io.securecodebox.persistence.defectdojo.model.Test.builder()
      .build();
    assertThat(sut.equalsQueryString(null), is(false));

    final var params = new HashMap<String, Object>();
    assertThat(sut.equalsQueryString(params), is(false));

    params.put("engagement", 42L);
    assertThat(sut.equalsQueryString(params), is(false));

    sut.setEngagement(23L);
    assertThat(sut.equalsQueryString(params), is(false));

    sut.setEngagement(42L);
    assertThat(sut.equalsQueryString(params), is(true));
  }
}
