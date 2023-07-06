// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.Response;
import io.securecodebox.persistence.defectdojo.model.ToolConfig;

public class ToolConfigService extends GenericDefectDojoService<ToolConfig> {
  public ToolConfigService(Config config) {
    super(config);
  }

  @Override
  protected String getUrlPath() {
    return "tool_configurations";
  }

  @Override
  protected Class<ToolConfig> getModelClass() {
    return ToolConfig.class;
  }

  @Override
  protected Response<ToolConfig> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
