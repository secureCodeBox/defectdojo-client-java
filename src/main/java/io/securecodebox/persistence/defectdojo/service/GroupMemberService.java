// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.Response;
import io.securecodebox.persistence.defectdojo.model.GroupMember;

public class GroupMemberService extends GenericDefectDojoService<GroupMember> {
  public GroupMemberService(Config config) {
    super(config);
  }

  @Override
  protected String getUrlPath() {
    return "dojo_group_members";
  }

  @Override
  protected Class<GroupMember> getModelClass() {
    return GroupMember.class;
  }

  @Override
  protected Response<GroupMember> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
