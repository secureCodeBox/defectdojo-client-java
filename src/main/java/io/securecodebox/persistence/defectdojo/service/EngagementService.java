// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.model.Engagement;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import lombok.NonNull;

public class EngagementService extends GenericDefectDojoService<Engagement> {
  public EngagementService(ClientConfig clientConfig) {
    super(clientConfig);
  }

  @Override
  protected String getUrlPath() {
    return "engagements";
  }

  @Override
  protected Class<Engagement> getModelClass() {
    return Engagement.class;
  }

  @Override
  protected PaginatedResult<Engagement> deserializeList(@NonNull String response) {
    try {
      return this.objectMapper.readValue(response, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new PersistenceException("Can't process JSON response!", e);
    }
  }
}
