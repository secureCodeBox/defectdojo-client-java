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
public final class Test implements Model, HasId {
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
  private Long testType;// FIXME: Use native type here.

  @JsonProperty
  private Long lead;// FIXME: Use native type here.

  @JsonProperty("percent_complete")
  private Long percentComplete;// FIXME: Use native type here.

  @JsonProperty
  private Long engagement;// FIXME: Use native type here.

  @JsonProperty
  private String version;

  /**
   * 1 Development
   * 3 Production
   */
  @JsonProperty
  @Builder.Default
  private Long environment = 1L;// FIXME: Use native type here.

  @Override
  public boolean equalsQueryString(Map<String, Object> queryParams) {
    if (QueryParamsComparator.isNull(queryParams)) {
      return false;
    }

    if (QueryParamsComparator.isIdEqual(this, queryParams)) {
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
