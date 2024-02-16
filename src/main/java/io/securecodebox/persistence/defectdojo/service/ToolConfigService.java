// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import io.securecodebox.persistence.defectdojo.model.ToolConfig;
import lombok.NonNull;

public class ToolConfigService extends GenericDefectDojoService<ToolConfig> {
  public ToolConfigService(ClientConfig clientConfig) {
    super(clientConfig);
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
  protected PaginatedResult<ToolConfig> deserializeList(@NonNull String response) {
    try {
      return this.objectMapper.readValue(response, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new PersistenceException("Can't process JSON response!", e);
    }
  }
}
