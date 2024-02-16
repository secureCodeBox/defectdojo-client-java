// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import io.securecodebox.persistence.defectdojo.model.ProductGroup;
import lombok.NonNull;

public class ProductGroupService extends GenericDefectDojoService<ProductGroup> {
  public ProductGroupService(ClientConfig clientConfig) {
    super(clientConfig);
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
  protected PaginatedResult<ProductGroup> deserializeList(@NonNull String response) {
    try {
      return this.objectMapper.readValue(response, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new PersistenceException("Can't process JSON response!", e);
    }
  }
}
