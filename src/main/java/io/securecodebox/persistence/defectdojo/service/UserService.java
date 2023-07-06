// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.Response;
import io.securecodebox.persistence.defectdojo.model.User;

public class UserService extends GenericDefectDojoService<User> {
  public UserService(Config config) {
    super(config);
  }

  @Override
  protected String getUrlPath() {
    return "users";
  }

  @Override
  protected Class<User> getModelClass() {
    return User.class;
  }

  @Override
  protected Response<User> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
