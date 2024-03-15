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
  /**
   * Query parameter name for id
   */
  static final String QUERY_PARAM_KEY_FOR_ID = "id";
  /**
   * Query parameter name for name
   */
  static final String QUERY_PARAM_KEY_FOR_NAME = "name";

  /**
   * Hidden for pure static helper class
   */
  private QueryParamsComparator() {
    super();
  }

  /**
   * Determines whether the given object is {@code null} or not
   *
   * @param o maybe {@code null}
   * @return {@code true} if {@code o} is {@code null}, else {@code false}
   */
  static boolean isNull(Object o) {
    return o == null;
  }

  /**
   * Determines whether the {@link HasId id} of the given model object is equal the given {@link #QUERY_PARAM_KEY_FOR_ID id}
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *    final var model = ...
   *    final var queryParams = new HashMap<String, Object>();
   *    queryParams.put(QueryParamsComparator.QUERY_PARAM_KEY_FOR_ID, 42);
   *
   *    if (QueryParamsComparator.isIdEqual(model, queryParams)) {
   *      ...
   *    }
   *   }
   * </pre>
   * <p>
   * TODO: What about type conversions? The id is a long in the models, but it may be a string in the map. Should it be treated as equal (42 == "42")?
   *
   * @param model       may be {@code null}
   * @param queryParams may be {@code null}
   * @return {@code true} id id is equal, else {@code false}
   */
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

    if (isNull(queryParams.get(QUERY_PARAM_KEY_FOR_ID))) {
      return false;
    }

    return queryParams.get(QUERY_PARAM_KEY_FOR_ID).equals(model.getId());
  }

  /**
   * Determines whether the {@link HasName name} of the given model object is equal the given {@link #QUERY_PARAM_KEY_FOR_NAME name}
   * <p>
   * Example:
   * </p>
   * <pre>
   *   {@code
   *    final var model = ...
   *    final var queryParams = new HashMap<String, Object>();
   *    queryParams.put(QueryParamsComparator.QUERY_PARAM_KEY_FOR_NAME, "foo");
   *
   *    if (QueryParamsComparator.isNameEqual(model, queryParams)) {
   *      ...
   *    }
   *   }
   * </pre>
   * <p>
   *
   * @param model       may be {@code null}
   * @param queryParams may be {@code null}
   * @return {@code true} if name is equal, else {@code false}
   */
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
