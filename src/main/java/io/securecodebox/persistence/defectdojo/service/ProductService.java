// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import io.securecodebox.persistence.defectdojo.model.Product;
import lombok.NonNull;

public class ProductService extends GenericDefectDojoService<Product> {

  public ProductService(ClientConfig clientConfig) {
    super(clientConfig);
  }

  @Override
  protected String getUrlPath() {
    return "products";
  }

  @Override
  protected Class<Product> getModelClass() {
    return Product.class;
  }

  @Override
  protected PaginatedResult<Product> deserializeList(@NonNull String response) {
    try {
      return modelObjectMapper().readValue(response, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new PersistenceException("Can't process JSON response!", e);
    }
  }
}
