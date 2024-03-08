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
public final class ToolConfig implements Model, HasId, HasName {
  @JsonProperty
  private long id;

  @JsonProperty
  String url;

  @JsonProperty
  @NonNull
  String name;

  @JsonProperty("tool_type")
  private long toolType;

  // FIXME: This is not present in my actual JSON response. Should remove?
  @JsonProperty("configuration_url")
  String configUrl;

  @JsonProperty
  String description;

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

    if (queryParams.containsKey("configuration_url") && queryParams.get("configuration_url").equals(this.configUrl)) {
      return true;
    }

    return false;
  }
}
