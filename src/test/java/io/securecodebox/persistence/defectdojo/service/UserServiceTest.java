package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.User;
import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link UserService}
 */
final class UserServiceTest  extends WireMockBaseTestCase{
  private final UserService sut = new UserService(conf());

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/users/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readResponseBodyFromFixture("io/securecodebox/persistence/defectdojo/service/fixture_UserService.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(3)),
      () -> assertThat(result, containsInAnyOrder(
        User.builder()
          .id(1)
          .username("admin")
          .firstName("Admin")
          .lastName("User")
          .build(),
        User.builder()
          .id(2)
          .username("JannikHollenbach")
          .firstName("Jannik")
          .lastName("Hollenbach")
          .build(),
        User.builder()
          .id(3)
          .username("SvenStrittmatter")
          .firstName("Sven")
          .lastName("Strittmatter")
          .build()
      ))
    );
  }
}
