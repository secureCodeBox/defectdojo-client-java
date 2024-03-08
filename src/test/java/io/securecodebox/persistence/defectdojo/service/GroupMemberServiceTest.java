package io.securecodebox.persistence.defectdojo.service;

import io.securecodebox.persistence.defectdojo.model.GroupMember;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * Tests for {@link GroupMemberService}
 */
final class GroupMemberServiceTest extends WireMockBaseTestCase {
  private final GroupMemberService sut = new GroupMemberService(conf());

  @Test
  void search() throws URISyntaxException, IOException {
    stubFor(
      get("/api/v2/dojo_group_members/?offset=0&limit=100")
        .willReturn(
          ok()
            .withBody(readResponseBodyFromFixture("GroupMemberService_response_fixture.json"))
        )
    );

    final var result = sut.search();

    assertAll(
      () -> assertThat(result, hasSize(7)),
      () -> assertThat(result, containsInAnyOrder(
        GroupMember.builder()
          .id(10)
          .group(10)
          .user(4)
          .role(3)
          .build(),
        GroupMember.builder()
          .id(1)
          .group(1)
          .user(2)
          .role(3)
          .build(),
        GroupMember.builder()
          .id(9)
          .group(9)
          .user(4)
          .role(3)
          .build(),
        GroupMember.builder()
          .id(4)
          .group(4)
          .user(4)
          .role(3)
          .build(),
        GroupMember.builder()
          .id(12)
          .group(1)
          .user(4)
          .role(3)
          .build(),
        GroupMember.builder()
          .id(6)
          .group(6)
          .user(4)
          .role(3)
          .build(),
        GroupMember.builder()
          .id(14)
          .group(1)
          .user(5)
          .role(3)
          .build()
      ))
    );
  }
}
