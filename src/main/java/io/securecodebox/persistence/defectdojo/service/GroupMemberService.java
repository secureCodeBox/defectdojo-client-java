// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.model.GroupMember;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import lombok.NonNull;

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
  protected PaginatedResult<GroupMember> deserializeList(String response) {
    try {
      return this.objectMapper.readValue(response, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new PersistenceException("Can't process JSON response!", e);
    }
  }
}
