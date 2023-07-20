package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link User}
 */
class UserTest {
    @Test
    @Disabled("#23 Fails due to wrong equals implementation")
    void equalsAndHashCode() {
        EqualsVerifier.forClass(User.class).verify();
    }
}
