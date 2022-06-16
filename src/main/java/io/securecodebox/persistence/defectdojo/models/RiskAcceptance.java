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

import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RiskAcceptance extends DefectDojoModel {
  @JsonProperty
  Long id;

  @JsonProperty
  String recommendation;

  @JsonProperty("recommendation_details")
  String recommendationDetails;

  String decision;

  @JsonProperty("decision_details")
  String decision_details;

  @JsonProperty
  String path;

  @JsonProperty
  String accepted_by;

  @JsonProperty
  String expiration_date;

  @JsonProperty
  Long expiration_date_warned;

  @JsonProperty
  Boolean expiration_date_handled;

  @JsonProperty("created")
  LocalDateTime createdAt;

  @JsonProperty("updated")
  LocalDateTime updatedAt;

  @JsonProperty
  Long owner;

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (queryParams.containsKey("id") && queryParams.get("id").equals(this.id)) {
      return true;
    }
    return false;
  }
}
