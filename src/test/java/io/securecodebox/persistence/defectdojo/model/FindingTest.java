package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Finding}
 */
class FindingTest {
  @Test
  void equalsAndHashCode() {
    EqualsVerifier.forClass(Finding.class)
      .suppress(Warning.NONFINAL_FIELDS)
      .verify();
  }
}
