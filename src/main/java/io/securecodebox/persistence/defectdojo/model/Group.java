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
public final class Group implements Model, HasId, HasName {
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
  @NonNull
  private String name;

  @JsonProperty
  private String description;

  @JsonProperty
  private List<Long> users;

  @JsonProperty("social_provider")
  private String socialProvider;

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (QueryParamsComparator.isNull(queryParams)) {
      return false;
    }

    if (QueryParamsComparator.isIdEqual(this, queryParams)) {
      return true;
    }

    if (QueryParamsComparator.isNameEqual(this, queryParams)) {
      return true;
    }

    return false;
  }
}
