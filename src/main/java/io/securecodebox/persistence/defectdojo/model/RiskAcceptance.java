// SPDX-FileCopyrightText: the secureCodeBox authors
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
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class RiskAcceptance implements Model, HasId {
  /**
   * Uniq id of model type
   * <p>
   * May be {@code null} for newly created objects because in DefectDojo's Open API specification i.
   * It is mandatory to use a boxed object type instead of a native type. A native type would result in 0 by
   * default which is a valid id for DefectDojo. Thus creating this type via POST request would try to create
   * one with id 0. Instead the id must be {@code null}, so that DefectDojo uses a newly generated uniq id.
   * </p>
   */
  @JsonProperty
  private Long id;

  @JsonProperty
  private String recommendation;

  @JsonProperty("recommendation_details")
  private String recommendationDetails;

  private String decision;

  @JsonProperty("decision_details")
  private String decisionDetails;

  @JsonProperty
  private String path;

  @JsonProperty("accepted_by")
  private String acceptedBy;

  @JsonProperty("expiration_date")
  private OffsetDateTime expirationDate;

  @JsonProperty("expiration_date_warned")
  private OffsetDateTime expirationDateWarned;

  @JsonProperty("expiration_date_handled")
  private OffsetDateTime expirationDateHandled;

  @JsonProperty("created")
  private OffsetDateTime createdAt;

  @JsonProperty("updated")
  private OffsetDateTime updatedAt;

  @JsonProperty
  private Long owner;// FIXME: Use native type here.

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (QueryParamsComparator.isNull(queryParams)) {
      return false;
    }

    return QueryParamsComparator.isIdEqual(this, queryParams);
  }
}
