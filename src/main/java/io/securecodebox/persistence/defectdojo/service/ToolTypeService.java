// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import io.securecodebox.persistence.defectdojo.model.ToolType;
import lombok.NonNull;

public class ToolTypeService extends GenericDefectDojoService<ToolType> {

  @Deprecated(forRemoval = true) // Unused
  public static final String GIT_SERVER_NAME = "Git Server";
  @Deprecated(forRemoval = true) // Unused
  public static final String BUILD_SERVER_NAME = "Build Server";
  @Deprecated(forRemoval = true) // Unused
  public static final String SECURITY_TEST_SERVER_NAME = "Security Test Orchestration Engine";

  public ToolTypeService(ClientConfig clientConfig) {
    super(clientConfig);
  }

  @Override
  protected String getUrlPath() {
    return "tool_types";
  }

  @Override
  protected Class<ToolType> getModelClass() {
    return ToolType.class;
  }

  @Override
  protected PaginatedResult<ToolType> deserializeList(@NonNull String response) {
    try {
      return this.objectMapper.readValue(response, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new PersistenceException("Can't process JSON response!", e);
    }
  }
}
