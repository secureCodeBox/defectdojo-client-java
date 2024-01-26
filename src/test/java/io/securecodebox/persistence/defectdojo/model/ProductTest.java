package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link Product}
 */
class ProductTest {
  @Test
  void equalsAndHashCode() {
    EqualsVerifier.forClass(Product.class)
      .suppress(Warning.NONFINAL_FIELDS)
      .verify();
  }
}
