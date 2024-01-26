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
public final class ToolType implements Model {
  @JsonProperty
  private long id;

  @JsonProperty
  @NonNull
  private String name;

  @JsonProperty
  private String description;

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (queryParams == null) {
      return false;
    }

    if (queryParams.containsKey("id") && queryParams.get("id").equals(this.id)) {
      return true;
    }

    if (queryParams.containsKey("name") && queryParams.get("name").equals(this.name)) {
      return true;
    }

    return false;
  }
}
