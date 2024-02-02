// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.ProductGroup;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;

public class ProductGroupService extends GenericDefectDojoService<ProductGroup> {
  public ProductGroupService(Config config) {
    super(config);
  }

  @Override
  protected String getUrlPath() {
    return "product_groups";
  }

  @Override
  protected Class<ProductGroup> getModelClass() {
    return ProductGroup.class;
  }

  @Override
  protected PaginatedResult<ProductGroup> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
