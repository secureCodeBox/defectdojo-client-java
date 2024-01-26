package io.securecodebox.persistence.defectdojo.model;

import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

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
