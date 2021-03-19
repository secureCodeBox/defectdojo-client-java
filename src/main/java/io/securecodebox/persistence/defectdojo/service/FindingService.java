/*
 *  secureCodeBox (SCB)
 *  Copyright 2021 iteratec GmbH
 *  https://www.iteratec.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.models.DefectDojoResponse;
import io.securecodebox.persistence.defectdojo.models.Finding;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FindingService extends GenericDefectDojoService<Finding> {
  public FindingService(DefectDojoConfig config) {
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
  protected DefectDojoResponse<Finding> deserializeList(String response) throws JsonProcessingException {
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
