// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.Response;
import io.securecodebox.persistence.defectdojo.model.Finding;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FindingService extends GenericDefectDojoService<Finding> {
  public FindingService(Config config) {
    super(config);
  }

  @Override
  protected String getUrlPath() {
    return "findings";
  }

  @Override
  protected Class<Finding> getModelClass() {
    return Finding.class;
  }

  @Override
  protected Response<Finding> deserializeList(String response) throws JsonProcessingException {
    return this.objectMapper.readValue(response, new TypeReference<>() {
    });
  }

  public List<Finding> getUnhandledFindingsForProduct(long productId, Finding.Severity minimumSeverity) throws URISyntaxException, JsonProcessingException {
    return this.search(Map.of("test__engagement__product", Long.toString(productId), "active", Boolean.toString(true))).stream().filter((finding -> {
      return finding.getSeverity().getNumericRepresentation() >= minimumSeverity.getNumericRepresentation();
    })).collect(Collectors.toList());
  }

  public List<Finding> getUnhandledFindingsForEngagement(long engagementId, Finding.Severity minimumSeverity) throws URISyntaxException, JsonProcessingException {
    return this.search(Map.of("test__engagement", Long.toString(engagementId), "active", Boolean.toString(true))).stream().filter((finding -> {
      return finding.getSeverity().getNumericRepresentation() >= minimumSeverity.getNumericRepresentation();
    })).collect(Collectors.toList());
  }
}
