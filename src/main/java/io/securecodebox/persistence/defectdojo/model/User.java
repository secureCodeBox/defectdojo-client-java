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
public final class User implements Model, HasId {
  @JsonProperty
  private long id;

  @JsonProperty
  @NonNull
  private String username;

  @JsonProperty("first_name")
  private String firstName;

  @JsonProperty("last_name")
  private String lastName;

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (QueryParamsComparator.isNull(queryParams)) {
      return false;
    }

    if (QueryParamsComparator.isIdEqual(this, queryParams)) {
      return true;
    }

    if (queryParams.containsKey("username") && queryParams.get("username").equals(this.username)) {
      return true;
    }

    return false;
  }
}
