// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.Group;
import io.securecodebox.persistence.defectdojo.model.Response;

public class GroupService extends GenericDefectDojoService<Group> {
  public GroupService(Config config) {
    super(config);
  }

  @Override
  protected String getUrlPath() {
    return "dojo_groups";
  }

  @Override
  protected Class<Group> getModelClass() {
    return Group.class;
  }

  @Override
  protected Response<Group> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
