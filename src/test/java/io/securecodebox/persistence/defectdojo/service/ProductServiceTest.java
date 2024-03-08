package io.securecodebox.persistence.defectdojo.service;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link ProductService}
 */
final class ProductServiceTest extends WireMockBaseTestCase{
  private final ProductService sut = new ProductService(conf());
}
