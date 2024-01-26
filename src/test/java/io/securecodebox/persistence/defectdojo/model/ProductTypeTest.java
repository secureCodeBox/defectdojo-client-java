package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ProductType}
 */
class ProductTypeTest {
  @Test
  void equalsAndHashCode() {
    EqualsVerifier.forClass(ProductType.class)
      .suppress(Warning.NONFINAL_FIELDS)
      .verify();
  }
}
