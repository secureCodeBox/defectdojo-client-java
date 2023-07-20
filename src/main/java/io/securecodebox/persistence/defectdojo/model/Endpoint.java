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
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Endpoint extends BaseModel {
  @JsonProperty
  Long id;

  @JsonProperty
  String protocol;

  @JsonProperty
  String host;

  @JsonProperty("fqdm")
  String fullyQualifiedDomainName;

  @JsonProperty
  Long port;

  @JsonProperty
  String path;

  @JsonProperty
  String query;

  @JsonProperty
  String fragment;

  @JsonProperty
  Long product;

  @JsonProperty
  Boolean mitigated;

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (queryParams.containsKey("id") && queryParams.get("id").equals(this.id)) {
      return true;
    }
    return false;
  }
}
