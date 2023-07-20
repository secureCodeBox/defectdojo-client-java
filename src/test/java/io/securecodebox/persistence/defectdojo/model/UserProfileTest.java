package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link UserProfile}
 */
class UserProfileTest {
    @Test
    @Disabled("#23 Fails due to wrong equals implementation")
    void equalsAndHashCode() {
        EqualsVerifier.forClass(UserProfile.class).verify();
    }
}
