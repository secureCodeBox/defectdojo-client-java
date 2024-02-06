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
   * TODO: What does this count? The number of results in one page or the total number?
   */
  @JsonProperty
  private int count;

  /**
   * TODO: What does this contain? I would expect a number for the next page.
   * <p>
   * This is {@code null} if there is no next page.
   * </p>
   */
  @JsonProperty
  private String next;

  /**
   * TODO: What does this contain? I would expect a number for the previous page.
   * <p>
   * This is {@code null} if there is no previous page.
   * </p>
   */
  @JsonProperty
  private String previous;

  @JsonProperty
  private List<T> results = new ArrayList<>();
}
