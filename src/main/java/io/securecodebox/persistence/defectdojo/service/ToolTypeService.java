// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.Response;
import io.securecodebox.persistence.defectdojo.model.ToolType;

public class ToolTypeService extends GenericDefectDojoService<ToolType> {

  public static final String GIT_SERVER_NAME = "Git Server";
  public static final String BUILD_SERVER_NAME = "Build Server";
  public static final String SECURITY_TEST_SERVER_NAME = "Security Test Orchestration Engine";

  public ToolTypeService(Config config) {
    super(config);
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
  protected Response<ToolType> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
