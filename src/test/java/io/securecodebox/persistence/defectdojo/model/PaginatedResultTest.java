package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link PaginatedResult}
 */
class PaginatedResultTest {
  @Test
  void equalsAndHashCode() {
    EqualsVerifier.forClass(PaginatedResult.class)
      .suppress(Warning.NONFINAL_FIELDS)
      .verify();
  }
}
