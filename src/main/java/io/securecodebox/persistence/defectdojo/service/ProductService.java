// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.Response;
import io.securecodebox.persistence.defectdojo.model.Product;

public class ProductService extends GenericDefectDojoService<Product> {

  public ProductService(Config config) {
    super(config);
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
  protected Response<Product> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
