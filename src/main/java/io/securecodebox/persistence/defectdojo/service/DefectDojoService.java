// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.model.Model;
import lombok.NonNull;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Basic CRUD interface for DefectDojo REST API
 *
 * @param <T> Type of model the implementation deals with
 */
public interface DefectDojoService<T extends Model> {
  /**
   * Get a single model object by its id from DefectDojo
   * <p>
   * TODO: Use Optional here instead of null as return value
   *
   * @param id must not be less than 1
   * @return maybe {@code null}
   */
  T get(long id);

  /**
   * Search for model objects by query parameters (name value pairs) in DefectDojo
   *
   * @param queryParams not {@code null}
   * @return not {@code null}, maybe empty
   * @throws URISyntaxException
   * @throws JsonProcessingException
   */
  List<T> search(@NonNull Map<String, Object> queryParams) throws URISyntaxException, JsonProcessingException;

  /**
   * Get list of all model objects in DefectDojo
   *
   * @return never {@code null}, maybe empty
   * @throws URISyntaxException
   * @throws JsonProcessingException
   */
  List<T> search() throws URISyntaxException, JsonProcessingException;

  /**
   * Search for a single model object in DefectDojo
   * <p>
   * If multiple objects were found the first one will be returned.
   * </p>
   *
   * @param searchObject not {@code null}
   * @return never {@code null}
   * @throws URISyntaxException
   * @throws JsonProcessingException
   */
  Optional<T> searchUnique(@NonNull T searchObject) throws URISyntaxException, JsonProcessingException;

  /**
   * Search for a single model object in DefectDojo
   * <p>
   * If multiple objects were found the first one will be returned.
   * </p>
   *
   * @param queryParams not {@code null}
   * @return never {@code null}
   * @throws URISyntaxException
   * @throws JsonProcessingException
   */
  Optional<T> searchUnique(@NonNull Map<String, Object> queryParams) throws URISyntaxException, JsonProcessingException;

  /**
   * Create the given model object in DefectDojo
   * <p>
   * Use the returned object for further processing because DefectDojo may alter it (e.g. the id).
   * </p>
   *
   * @param object not {@code null}
   * @return never {@code null}
   */
  T create(@NonNull T object);

  /**
   * Delete the given model object in DefectDojo identified by its id
   *
   * @param id must not be less than 1
   */
  void delete(long id);

  /**
   * Update the given model object in DefectDojo identified by its id
   *
   * <p>
   * Use the returned object for further processing because DefectDojo may alter it (e.g. the id).
   * </p>
   *
   * @param object not {@code null}
   * @param id     must not be less than 1
   * @return never {@code null}
   */
  T update(@NonNull T object, long id);
}
