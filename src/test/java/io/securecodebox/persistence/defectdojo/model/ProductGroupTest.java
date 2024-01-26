package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ProductGroup}
 */
class ProductGroupTest {
  @Test
  void equalsAndHashCode() {
    EqualsVerifier.forClass(ProductGroup.class)
      .suppress(Warning.NONFINAL_FIELDS)
      .verify();
  }
}
