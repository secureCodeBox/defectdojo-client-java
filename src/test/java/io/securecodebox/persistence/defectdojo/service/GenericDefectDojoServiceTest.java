package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.model.Model;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import lombok.NonNull;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * Tests for {@link GenericDefectDojoService}
 */
final class GenericDefectDojoServiceTest extends WireMockBaseTestCase {
  private static final class TestModel implements Model {
    @Override
    public boolean equalsQueryString(@NonNull Map<String, Object> queryParams) {
      // Stub this to false because we do not test this method here.
      return false;
    }
  }
  private final GenericDefectDojoService<TestModel> sut =new GenericDefectDojoService<>(conf()) {

    @Override
    protected String getUrlPath() {
      return "snafu";
    }

    @Override
    protected Class<TestModel> getModelClass() {
      return TestModel.class;
    }

    @Override
    protected PaginatedResult<TestModel> deserializeList(@NonNull String response) {
      try {
        return this.objectMapper.readValue(response, new TypeReference<>() {
        });
      } catch (JsonProcessingException e) {
        throw new PersistenceException("Can't process JSON response!", e);
      }
    }
  };

  @Test
  void createBaseUrl() {
    assertThat(sut.createBaseUrl(), is(URI.create("http://localhost:8888/api/v2/snafu/")));
  }
}
