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
public final class UserProfile implements Model {

  @JsonProperty
  private User user;

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    // The user_profile endpoint does not have query parameters that's why this function will just return true
    return true;
  }

}
