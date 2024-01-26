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
public final class Endpoint implements Model {
  @JsonProperty
  private Long id;// FIXME: Use native type here.

  @JsonProperty
  private String protocol;

  @JsonProperty
  private String host;

  @JsonProperty("fqdm")
  private String fullyQualifiedDomainName;

  @JsonProperty
  private Long port;// FIXME: Use native type here.

  @JsonProperty
  private String path;

  @JsonProperty
  private String query;

  @JsonProperty
  private String fragment;

  @JsonProperty
  private Long product;// FIXME: Use native type here.

  @JsonProperty
  private Boolean mitigated;// FIXME: Use native type here.

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
      return queryParams.containsKey("id") && queryParams.get("id").equals(this.id);
  }
}
