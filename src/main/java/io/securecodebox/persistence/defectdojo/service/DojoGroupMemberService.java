// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.model.DefectDojoResponse;
import io.securecodebox.persistence.defectdojo.model.DojoGroupMember;

public class DojoGroupMemberService extends GenericDefectDojoService<DojoGroupMember> {
  public DojoGroupMemberService(DefectDojoConfig config) {
    super(config);
  }

  @Override
  protected String getUrlPath() {
    return "dojo_group_members";
  }

  @Override
  protected Class<DojoGroupMember> getModelClass() {
    return DojoGroupMember.class;
  }

  @Override
  protected DefectDojoResponse<DojoGroupMember> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
