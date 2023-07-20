package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link Engagement}
 */
class EngagementTest {
    @Test
    @Disabled("#23 Fails due to wrong equals implementation")
    void equalsAndHashCode() {
        EqualsVerifier.forClass(Engagement.class).verify();
    }
}
