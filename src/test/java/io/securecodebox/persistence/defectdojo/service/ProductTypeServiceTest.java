// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.ProductType;
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
 * Tests for {@link ProductTypeService}
 */
final class ProductTypeServiceTest extends WireMockBaseTestCase {
  private final ProductTypeService sut = new ProductTypeService(conf());

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/product_types/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readFixtureFile("ProductTypeService_response_fixture.json"))
        )
    );

    var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(2)),
      () -> assertThat(result, containsInAnyOrder(
        ProductType.builder()
          .id(1)
          .name("Research and Development")
          .criticalProduct(true)
          .keyProduct(false)
          .build(),
        ProductType.builder()
          .id(2)
          .name("secureCodeBox")
          .criticalProduct(false)
          .keyProduct(false)
          .build()
      ))
    );
  }

  @Test
  @Disabled("TODO: Implement test.")
  void search_withQueryParams() {
  }

  @Test
  @Disabled("TODO: Implement test.")
  void get_byId() {
  }


  @Test
  @Disabled("TODO: Implement test.")
  void searchUnique_withSearchObject() {
  }

  @Test
  @Disabled("TODO: Implement test.")
  void searchUnique_withQueryParams() {
  }

  @Test
  @Disabled("TODO: Implement test.")
  void create() {
  }

  @Test
  @Disabled("TODO: Implement test.")
  void delete() {
  }

  @Test
  @Disabled("TODO: Implement test.")
  void update() {
  }
}
