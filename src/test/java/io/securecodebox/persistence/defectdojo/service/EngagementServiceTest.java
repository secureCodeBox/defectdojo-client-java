package io.securecodebox.persistence.defectdojo.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link EngagementService}
 */
final class EngagementServiceTest extends WireMockBaseTestCase {
  private final EngagementService sut = new EngagementService(conf());
}
