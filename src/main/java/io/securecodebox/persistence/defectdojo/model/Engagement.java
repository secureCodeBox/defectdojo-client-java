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
  private String name;

  @JsonProperty("branch_tag")
  @Builder.Default
  private String branch = "";

  @JsonProperty
  private Long product;// FIXME: Use native type here.

  @JsonProperty("target_start")
  private String targetStart;

  @JsonProperty("target_end")
  private String targetEnd;

  @JsonProperty
  private Long lead;// FIXME: Use native type here.

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
  private Long buildServer; // FIXME: Use native type here.

  @JsonProperty("source_code_management_server")
  private Long scmServer; // FIXME: Use natvive type here.

  @JsonProperty("orchestration_engine")
  private Long orchestrationEngine; // FIXME: Use natvive type here.

  @JsonProperty
  @Builder.Default
  private String description = "";

  @JsonProperty("deduplication_on_engagement")
  private boolean deduplicationOnEngagement;

  @JsonProperty("threat_model")
  @Builder.Default // FIXME: Use native type here.
  private Boolean threatModel = false;

  @JsonProperty("api_test")
  @Builder.Default // FIXME: Use native type here.
  private Boolean apiTest = false;

  @JsonProperty("pen_test")
  @Builder.Default // FIXME: Use native type here.
  private Boolean penTest = false;

  @JsonProperty("check_list")
  @Builder.Default // FIXME: Use native type here.
  private Boolean checkList = false;

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
