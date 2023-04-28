// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.models.DefectDojoResponse;
import io.securecodebox.persistence.defectdojo.models.Test;

public class TestService extends GenericDefectDojoService<Test> {
  public TestService(DefectDojoConfig config) {
    super(config);
  }

  @Override
  protected String getUrlPath() {
    return "tests";
  }

  @Override
  protected Class<Test> getModelClass() {
    return Test.class;
  }

  @Override
  protected DefectDojoResponse<Test> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
