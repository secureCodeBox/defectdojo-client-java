package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link RiskAcceptance}
 */
class RiskAcceptanceTest {
    @Test
    @Disabled("#23 Fails due to wrong equals implementation")
    void equalsAndHashCode() {
        EqualsVerifier.forClass(RiskAcceptance.class).verify();
    }
}
