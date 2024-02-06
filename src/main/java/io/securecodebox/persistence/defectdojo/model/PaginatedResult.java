// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * This class wraps the paginated results from DefectDojo
 *
 * @param <T> type of results
 */
@Data
public final class PaginatedResult<T extends Model> {
  /**
   * Total number of results
   */
  @JsonProperty
  private int count;

  /**
   * URL to the result's next page
   * <p>
   * This is {@code null} if there is no next page.
   * </p>
   */
  @JsonProperty
  private String next;

  /**
   * URL to the result's previous page
   * <p>
   * This is {@code null} if there is no previous page.
   * </p>
   */
  @JsonProperty
  private String previous;

  /**
   * Result for current page
   * <p>
   * Never {@code null}.
   * </p>
   */
  @JsonProperty
  private List<T> results = new ArrayList<>();
}
