// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.ClientConfig;
import io.securecodebox.persistence.defectdojo.exception.PersistenceException;
import io.securecodebox.persistence.defectdojo.model.Finding;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import lombok.NonNull;

import java.util.List;
import java.util.Map;

public class FindingService extends GenericDefectDojoService<Finding> {
  public FindingService(ClientConfig clientConfig) {
    super(clientConfig);
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
  protected PaginatedResult<Finding> deserializeList(@NonNull String response) {
    try {
      return modelObjectMapper().readValue(response, new TypeReference<>() {
      });
    } catch (JsonProcessingException e) {
      throw new PersistenceException("Can't process JSON response!", e);
    }
  }

  public List<Finding> getUnhandledFindingsForProduct(long productId, Finding.Severity minimumSeverity) {
    final Map<String, Object> queryParams = Map.of(
      "test__engagement__product", Long.toString(productId),
      "active", Boolean.toString(true));

    return this.search(queryParams)
      .stream()
      .filter((finding -> finding.getSeverity().getNumericRepresentation() >= minimumSeverity.getNumericRepresentation()))
      .toList();
  }

  public List<Finding> getUnhandledFindingsForEngagement(long engagementId, Finding.Severity minimumSeverity) {
    final Map<String, Object> queryParams = Map.of(
      "test__engagement", Long.toString(engagementId),
      "active", Boolean.toString(true));

    return this.search(queryParams)
      .stream()
      .filter((finding -> finding.getSeverity().getNumericRepresentation() >= minimumSeverity.getNumericRepresentation()))
      .toList();
  }
}
