package io.securecodebox.persistence.defectdojo.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ToolTypeService}
 */
final class ToolTypeServiceTest extends WireMockBaseTestCase {
  private final ToolTypeService sut = new ToolTypeService(conf());
}
