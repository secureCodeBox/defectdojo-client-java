// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.Product;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link ProductService}
 */
final class ProductServiceTest extends WireMockBaseTestCase {
  private final ProductService sut = new ProductService(conf());

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/products/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readFixtureFile("ProductService_response_fixture.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(3)),
      () -> assertThat(result, containsInAnyOrder(
        Product.builder()
          .id(419)
          .name("10.0.0.1")
          .description("Product was automatically created by the secureCodeBox DefectDojo integration")
          .tags(List.of(
            "attack-surface/internal",
            "office/munich",
            "org/owasp",
            "vlan/dev"
          ))
          .productType(2)
          .findingsCount(12)
          .enableFullRiskAcceptance(true)
          .authorizationGroups(Collections.emptyList())
          .build(),
        Product.builder()
          .id(312)
          .name("10.0.0.2")
          .description("Product was automatically created by the secureCodeBox DefectDojo integration")
          .tags(List.of(
            "attack-surface/internal",
            "office/hamburg",
            "org/owasp",
            "vlan/dev"
          ))
          .productType(1)
          .findingsCount(16)
          .enableFullRiskAcceptance(false)
          .authorizationGroups(List.of(1L, 2L, 3L))
          .build(),
        Product.builder()
          .id(297)
          .name("10.0.0.3")
          .description("Product was automatically created by the secureCodeBox DefectDojo integration")
          .tags(List.of(
            "attack-surface/external",
            "office/munich",
            "org/owasp"
          ))
          .productType(2)
          .findingsCount(16)
          .enableSimpleRiskAcceptance(true)
          .authorizationGroups(Collections.emptyList())
          .build()
      ))
    );
  }
}
