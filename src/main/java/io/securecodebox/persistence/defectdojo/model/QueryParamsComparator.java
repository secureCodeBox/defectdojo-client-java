// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import java.util.Map;

/**
 * Pure static helper class
 * <p>
 * This type is package private because it is an implementation detail of the models and
 * should not be used outside of this package.
 * </p>
 */
final class QueryParamsComparator {

  static final String QUERY_PARAM_KEY_FOR_ID = "id";
  static final String QUERY_PARAM_KEY_FOR_NAME = "name";

  private QueryParamsComparator() {
    super();
  }

  static boolean isNull(Object o) {
    return o == null;
  }

  static boolean isIdEqual(HasId model, Map<String, Object> queryParams) {
    if (isNull(model)) {
      return false;
    }

    if (isNull(queryParams)) {
      return false;
    }

    if (!queryParams.containsKey(QUERY_PARAM_KEY_FOR_ID)) {
      return false;
    }

    // FIXME: Since th generic type for value is Object, possible NPE here!
    return queryParams.get(QUERY_PARAM_KEY_FOR_ID).equals(model.getId());
  }

  static boolean isNameEqual(HasName model, Map<String, Object> queryParams) {
    if (isNull(model)) {
      return false;
    }

    if (isNull(queryParams)) {
      return false;
    }

    if (!queryParams.containsKey(QUERY_PARAM_KEY_FOR_NAME)) {
      return false;
    }

    if (isNull(queryParams.get(QUERY_PARAM_KEY_FOR_NAME))) {
      return false;
    }

    return queryParams.get(QUERY_PARAM_KEY_FOR_NAME).equals(model.getName());
  }
}
