// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Test extends DefectDojoModel {
  @JsonProperty
  Long id;

  @JsonProperty
  String title;

  @JsonProperty
  String description;

  @JsonProperty("target_start")
  String targetStart;

  @JsonProperty("target_end")
  String targetEnd;

  @JsonProperty
  @Builder.Default
  List<String> tags = new LinkedList<>();

  @JsonProperty("test_type")
  Long testType;

  @JsonProperty
  Long lead;

  @JsonProperty("percent_complete")
  Long percentComplete;

  @JsonProperty
  Long engagement;

  @JsonProperty
  String version;

  /**
   * 1 Development
   * 3 Production
   */
  @JsonProperty
  @Builder.Default
  Long environment = 1L;

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (queryParams.containsKey("id") && queryParams.get("id").equals(this.id)) {
      return true;
    }
    if (queryParams.containsKey("title") && queryParams.get("title").equals(this.title)) {
      return true;
    }
    if (queryParams.containsKey("engagement") && queryParams.get("engagement").equals(this.engagement)) {
      return true;
    }

    return false;
  }
}
