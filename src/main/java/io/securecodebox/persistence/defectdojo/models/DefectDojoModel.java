// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.models;

import java.util.Map;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode // FIXME: Implement hashCode/equals in inheritance is problematic (https://www.artima.com/articles/how-to-write-an-equality-method-in-java)
abstract public class DefectDojoModel {
  public abstract boolean equalsQueryString(Map<String, Object> queryParams);
}
