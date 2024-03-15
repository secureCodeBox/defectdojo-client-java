// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.model.Endpoint;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import lombok.NonNull;

public class EndpointService extends GenericDefectDojoService<Endpoint> {
  public EndpointService(ClientConfig clientConfig) {
    super(clientConfig);
  }

  @Override
  protected String getUrlPath() {
    return "endpoints";
  }

  @Override
  protected Class<Endpoint> getModelClass() {
    return Endpoint.class;
  }

  @Override
  protected PaginatedResult<Endpoint> deserializeList(@NonNull String response) {
    try {
      return modelObjectMapper().readValue(response, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new PersistenceException("Can't process JSON response!", e);
    }
  }
}
