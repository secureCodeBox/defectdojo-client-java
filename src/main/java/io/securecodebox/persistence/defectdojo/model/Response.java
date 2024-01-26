// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

// TODO: Why we do not have as many annotations as the other models here?
// TODO: Why does this class does not implement Model?
@Data
public final class Response<T> {
  @JsonProperty
  private int count;

  @JsonProperty
  private String next;

  @JsonProperty
  private String previous;

  @JsonProperty
  private List<T> results;
}
