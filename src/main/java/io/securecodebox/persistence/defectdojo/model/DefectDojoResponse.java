// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DefectDojoResponse<T> {
  @JsonProperty
  int count;

  @JsonProperty
  String next;

  @JsonProperty
  String previous;

  @JsonProperty
  List<T> results;
}
