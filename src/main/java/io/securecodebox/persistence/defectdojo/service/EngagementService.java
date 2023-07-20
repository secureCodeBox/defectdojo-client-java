// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.Response;
import io.securecodebox.persistence.defectdojo.model.Engagement;

public class EngagementService extends GenericDefectDojoService<Engagement> {
  public EngagementService(Config config) {
    super(config);
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
  protected Response<Engagement> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
