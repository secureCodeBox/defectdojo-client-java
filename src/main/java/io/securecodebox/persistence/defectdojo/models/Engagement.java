/*
 *  secureCodeBox (SCB)
 *  Copyright 2021 iteratec GmbH
 *  https://www.iteratec.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.securecodebox.persistence.defectdojo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Engagement extends DefectDojoModel {
  @JsonProperty("branch_tag")
  public String branch;

  @JsonProperty
  protected Long id;

  @JsonProperty
  protected String name;

  @JsonProperty
  protected Long product;

  @JsonProperty("target_start")
  protected String targetStart;

  @JsonProperty("target_end")
  protected String targetEnd;

  @JsonProperty
  protected Long lead;

  @JsonProperty("engagement_type")
  @Builder.Default
  protected String engagementType = "CI/CD";

  @JsonProperty
  @Builder.Default
  protected Status status = Status.IN_PROGRESS;

  @JsonProperty
  protected List<String> tags;

  @JsonProperty
  protected String tracker;

  @JsonProperty("build_id")
  protected String buildID;

  @JsonProperty("commit_hash")
  protected String commitHash;

  @JsonProperty("source_code_management_uri")
  protected String repo;

  @JsonProperty("build_server")
  protected Long buildServer;

  @JsonProperty("source_code_management_server")
  protected Long scmServer;

  @JsonProperty("orchestration_engine")
  protected Long orchestrationEngine;

  @JsonProperty
  protected String description;

  @JsonProperty("deduplication_on_engagement")
  protected boolean deduplicationOnEngagement;

  @JsonProperty("threat_model")
  @Builder.Default
  protected Boolean threatModel = false;

  @JsonProperty("api_test")
  @Builder.Default
  protected Boolean apiTest = false;

  @JsonProperty("pen_test")
  @Builder.Default
  protected Boolean penTest = false;

  @JsonProperty("check_list")
  @Builder.Default
  protected Boolean checkList = false;

  @JsonProperty
  protected String version;

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (queryParams == null) {
      return false;
    }
    if (queryParams.containsKey("id") && queryParams.get("id") != null && queryParams.get("id").equals(this.id)) {
      return true;
    }
    if (queryParams.containsKey("name") && queryParams.get("name") != null && queryParams.get("name").equals(this.name)) {
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
