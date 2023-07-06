// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Product extends DefectDojoModel {
  @JsonProperty
  Long id;

  @JsonProperty
  String name;

  @JsonProperty
  List<String> tags;

  @JsonProperty
  String description;

  @JsonProperty("findings_count")
  Long findingsCount;

  @JsonProperty("authorized_users")
  List<String> authorizedUsers;

  @JsonProperty("prod_type")
  Long productType;
  
  @JsonProperty("enable_simple_risk_acceptance")
  Boolean enableSimpleRiskAcceptance;
  
  @JsonProperty("enable_full_risk_acceptance")
  Boolean enableFullRiskAcceptance;

  @JsonProperty("authorization_groups")
  List<Long> authorizationGroups;
  
  @JsonProperty
  String lifecycle;

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (queryParams.containsKey("id") && queryParams.get("id").equals(this.id)) {
      return true;
    }
    if (queryParams.containsKey("name") && queryParams.get("name").equals(this.name)) {
      return true;
    }

    return false;
  }
}
