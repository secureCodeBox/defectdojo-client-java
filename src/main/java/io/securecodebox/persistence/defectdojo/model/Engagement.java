// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Engagement implements Model, HasId, HasName {
  @JsonProperty("branch_tag")
  @Builder.Default
  private String branch = "";

  @JsonProperty
  private long id;

  @JsonProperty
  private String name;

  @JsonProperty
  private long product;

  @JsonProperty("target_start")
  private String targetStart;

  @JsonProperty("target_end")
  private String targetEnd;

  @JsonProperty
  private long lead;

  @JsonProperty("engagement_type")
  @Builder.Default
  private String engagementType = "CI/CD";

  @JsonProperty
  @Builder.Default
  private Status status = Status.IN_PROGRESS;

  @JsonProperty
  @Builder.Default
  private List<String> tags = new ArrayList<>();

  @JsonProperty
  private String tracker;

  @JsonProperty("build_id")
  private String buildID;

  @JsonProperty("commit_hash")
  private String commitHash;

  @JsonProperty("source_code_management_uri")
  private String repo;

  @JsonProperty("build_server")
  private long buildServer;

  @JsonProperty("source_code_management_server")
  private long scmServer;

  @JsonProperty("orchestration_engine")
  private long orchestrationEngine;

  @JsonProperty
  @Builder.Default
  private String description = "";

  @JsonProperty("deduplication_on_engagement")
  private boolean deduplicationOnEngagement;

  @JsonProperty("threat_model")
  private boolean threatModel;

  @JsonProperty("api_test")
  private boolean apiTest;

  @JsonProperty("pen_test")
  private boolean penTest;

  @JsonProperty("check_list")
  private boolean checkList;

  @JsonProperty
  @Builder.Default
  private String version = "";

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

  /**
   * Currently only contains the statuses relevant to us.
   * If you need others, feel free to add them ;)
   */
  public enum Status {
    @JsonProperty("Completed")
    COMPLETED,
    @JsonProperty("In Progress")
    IN_PROGRESS
  }
}
