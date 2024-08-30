// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Endpoint implements Model, HasId {
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
  private String protocol;

  @JsonProperty
  private String host;

  @JsonProperty("fqdm")
  private String fullyQualifiedDomainName;

  @JsonProperty
  private Long port;// FIXME: Use native type here.

  @JsonProperty
  private String path;

  @JsonProperty
  private String query;

  @JsonProperty
  private String fragment;

  @JsonProperty
  private Long product;// FIXME: Use native type here.

  @JsonProperty
  private Boolean mitigated;// FIXME: Use native type here.

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (QueryParamsComparator.isNull(queryParams)) {
      return false;
    }

    return QueryParamsComparator.isIdEqual(this, queryParams);
  }

}
