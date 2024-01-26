package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link ToolConfig}
 */
class ToolConfigTest {
  @Test
  void equalsAndHashCode() {
    EqualsVerifier.forClass(ToolConfig.class)
      .suppress(Warning.NONFINAL_FIELDS)
      .verify();
  }
}
