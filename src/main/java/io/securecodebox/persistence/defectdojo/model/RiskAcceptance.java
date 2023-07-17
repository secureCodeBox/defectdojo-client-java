// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskAcceptance extends BaseModel {
  @JsonProperty
  Long id;

  @JsonProperty
  String recommendation;

  @JsonProperty("recommendation_details")
  String recommendationDetails;

  String decision;

  @JsonProperty("decision_details")
  String decisionDetails;

  @JsonProperty
  String path;

  @JsonProperty("accepted_by")
  String acceptedBy;

  @JsonProperty("expiration_date")
  OffsetDateTime expirationDate;

  @JsonProperty("expiration_date_warned")
  OffsetDateTime expirationDateWarned;

  @JsonProperty("expiration_date_handled")
  OffsetDateTime expirationDateHandled;

  @JsonProperty("created")
  OffsetDateTime createdAt;

  @JsonProperty("updated")
  OffsetDateTime updatedAt;

  @JsonProperty
  Long owner;

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (queryParams.containsKey("id") && queryParams.get("id").equals(this.id)) {
      return true;
    }
    return false;
  }
}
