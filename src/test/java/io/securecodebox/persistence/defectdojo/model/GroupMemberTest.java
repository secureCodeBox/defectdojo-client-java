package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link GroupMember}
 */
class GroupMemberTest {
  @Test
  void equalsAndHashCode() {
    EqualsVerifier.forClass(GroupMember.class)
      .suppress(Warning.NONFINAL_FIELDS)
      .verify();
  }
}
