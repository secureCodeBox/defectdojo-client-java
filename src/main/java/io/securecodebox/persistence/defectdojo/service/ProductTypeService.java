// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import io.securecodebox.persistence.defectdojo.model.ProductType;
import lombok.NonNull;

public final class ProductTypeService extends GenericDefectDojoService<ProductType> {

  public ProductTypeService(Config config) {
    super(config);
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
  protected PaginatedResult<ProductType> deserializeList(@NonNull String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
