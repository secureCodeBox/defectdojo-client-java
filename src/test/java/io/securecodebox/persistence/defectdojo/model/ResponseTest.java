package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Response}
 */
class ResponseTest {
  @Test
  void equalsAndHashCode() {
    EqualsVerifier.forClass(Response.class)
      .suppress(Warning.NONFINAL_FIELDS)
      .verify();
  }
}
