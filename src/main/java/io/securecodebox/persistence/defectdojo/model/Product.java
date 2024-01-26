// SPDX-FileCopyrightText: the secureCodeBox authors
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
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Product implements Model {
  @JsonProperty
  private long id;

  @JsonProperty
  private String name;

  @JsonProperty
  private List<String> tags;

  @JsonProperty
  private String description;

  @JsonProperty("findings_count")
  private long findingsCount;

  @JsonProperty("authorized_users")
  private List<String> authorizedUsers;

  @JsonProperty("prod_type")
  private long productType;
  
  @JsonProperty("enable_simple_risk_acceptance")
  private boolean enableSimpleRiskAcceptance;
  
  @JsonProperty("enable_full_risk_acceptance")
  private boolean enableFullRiskAcceptance;

  @JsonProperty("authorization_groups")
  private List<Long> authorizationGroups;
  
  @JsonProperty
  private String lifecycle;

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
