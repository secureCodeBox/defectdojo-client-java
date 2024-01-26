package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link UserProfile}
 */
class UserProfileTest {
    @Test
    void equalsAndHashCode() {
        EqualsVerifier.forClass(UserProfile.class)
          .suppress(Warning.NONFINAL_FIELDS)
          .verify();
    }
}
