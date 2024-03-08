// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0
package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.securecodebox.persistence.defectdojo.model.Model;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Basic CRUD interface for DefectDojo REST API
 */
public interface DefectDojoService<T extends Model> {
  T get(long id);

  List<T> search(Map<String, Object> queryParams) throws URISyntaxException, JsonProcessingException;

  List<T> search() throws URISyntaxException, JsonProcessingException;

  Optional<T> searchUnique(T searchObject) throws URISyntaxException, JsonProcessingException;

  Optional<T> searchUnique(Map<String, Object> queryParams) throws URISyntaxException, JsonProcessingException;

  T create(T object);

  void delete(long id);

  T update(T object, long objectId);
}
