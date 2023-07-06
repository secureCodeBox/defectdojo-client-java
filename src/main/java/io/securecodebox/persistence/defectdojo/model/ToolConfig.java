// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
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
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ToolConfig extends DefectDojoModel {
  @JsonProperty
  Long id;

  @JsonProperty
  String url;

  @JsonProperty
  @NonNull
  String name;

  @JsonProperty("tool_type")
  Long toolType;

  @JsonProperty("configuration_url")
  String configUrl;

  @JsonProperty
  String description;

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (queryParams.containsKey("id") && queryParams.get("id").equals(this.id)) {
      return true;
    }
    if (queryParams.containsKey("name") && queryParams.get("name").equals(this.name)) {
      return true;
    }
    if (queryParams.containsKey("configuration_url") && queryParams.get("configuration_url").equals(this.configUrl)) {
      return true;
    }

    return false;
  }
}
