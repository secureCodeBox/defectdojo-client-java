package io.securecodebox.persistence.defectdojo.model;

import java.util.Map;

/**
 * Interface for all models
 */
public interface Model {
  boolean equalsQueryString(Map<String, Object> queryParams);
}
