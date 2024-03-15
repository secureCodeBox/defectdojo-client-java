// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import io.securecodebox.persistence.defectdojo.model.ProductType;
import lombok.NonNull;

public final class ProductTypeService extends GenericDefectDojoService<ProductType> {

  public ProductTypeService(ClientConfig clientConfig) {
    super(clientConfig);
  }

  @Override
  protected String getUrlPath() {
    return "product_types";
  }

  @Override
  protected Class<ProductType> getModelClass() {
    return ProductType.class;
  }

  @Override
  protected PaginatedResult<ProductType> deserializeList(@NonNull String response) {
    try {
      return modelObjectMapper().readValue(response, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new PersistenceException("Can't process JSON response!", e);
    }
  }
}
