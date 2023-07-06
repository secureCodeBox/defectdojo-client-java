// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.model.DefectDojoResponse;
import io.securecodebox.persistence.defectdojo.model.DojoGroup;

public class DojoGroupService extends GenericDefectDojoService<DojoGroup> {
  public DojoGroupService(DefectDojoConfig config) {
    super(config);
  }

  @Override
  protected String getUrlPath() {
    return "dojo_groups";
  }

  @Override
  protected Class<DojoGroup> getModelClass() {
    return DojoGroup.class;
  }

  @Override
  protected DefectDojoResponse<DojoGroup> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
