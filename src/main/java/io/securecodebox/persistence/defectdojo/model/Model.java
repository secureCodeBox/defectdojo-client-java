package io.securecodebox.persistence.defectdojo.model;

import java.util.Map;

/**
 * Interface for all models
 */
public interface Model {
  /**
   * Compares this model with the given query parameters
   * <p>
   *   The given query parameters are name value pairs, e.g.:
   * </p>
   *
   * TODO: Should we annotate queryParams with {@code @NonNull}?
   * @param queryParams may be {@code null}
   * @return {@code true} if equal, elas {@code false}
   */
  boolean equalsQueryString(Map<String, Object> queryParams);
}
