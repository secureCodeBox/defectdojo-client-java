package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

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
}
