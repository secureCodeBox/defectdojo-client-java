// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.model.DefectDojoResponse;
import io.securecodebox.persistence.defectdojo.model.ProductType;

public class ProductTypeService extends GenericDefectDojoService<ProductType> {

  public ProductTypeService(DefectDojoConfig config) {
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
  protected DefectDojoResponse<ProductType> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }
}
