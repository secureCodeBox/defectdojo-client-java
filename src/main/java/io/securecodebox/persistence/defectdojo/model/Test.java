// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

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
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Test implements Model {
  @JsonProperty
  private long id;

  @JsonProperty
  private String title;

  @JsonProperty
  private String description;

  @JsonProperty("target_start")
  private String targetStart;

  @JsonProperty("target_end")
  private String targetEnd;

  @JsonProperty
  @Builder.Default
  private List<String> tags = new LinkedList<>();

  @JsonProperty("test_type")
  private long testType;

  @JsonProperty
  private long lead;

  @JsonProperty("percent_complete")
  private long percentComplete;

  @JsonProperty
  private long engagement;

  @JsonProperty
  private String version;

  /**
   * 1 Development
   * 3 Production
   */
  @JsonProperty
  @Builder.Default
  private long environment = 1L;

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
