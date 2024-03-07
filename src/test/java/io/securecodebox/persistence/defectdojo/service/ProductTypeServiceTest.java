package io.securecodebox.persistence.defectdojo.service;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.ProductType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link ProductTypeService}
 */
@WireMockTest(httpPort = ProductTypeServiceTest.PORT)
class ProductTypeServiceTest {
  public static final int PORT = 8888;
  private final Config conf = new Config(
    String.format("http://localhost:%d/", PORT),
    "not-required-for-tests");
  private final ProductTypeService sut = new ProductTypeService(conf);

  private String readResponseBodyFromFixture(String name) throws IOException {
    try (final var input = getClass().getClassLoader().getResourceAsStream(name)) {
      final var bytes = Objects.requireNonNull(input).readAllBytes();
      return new String(bytes, StandardCharsets.UTF_8);
    }
  }

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/product_types/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readResponseBodyFromFixture("io/securecodebox/persistence/defectdojo/service/fixture_ProductTypeService.json"))
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
}
