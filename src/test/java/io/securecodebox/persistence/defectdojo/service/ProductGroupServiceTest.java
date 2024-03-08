package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.ProductGroup;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link ProductGroupService}
 */
final class ProductGroupServiceTest extends WireMockBaseTestCase {
  private final ProductGroupService sut = new ProductGroupService(conf());

  @Test
  @Disabled("TODO: Add non-empty fixture for ProductGroupService.")
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/product_groups/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readResponseBodyFromFixture("ProductGroupService_response_fixture.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(0)),
      () -> assertThat(result, containsInAnyOrder(
        ProductGroup.builder().build()
      ))
    );
  }
}
