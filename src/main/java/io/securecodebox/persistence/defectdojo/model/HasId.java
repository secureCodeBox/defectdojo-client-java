// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

/**
 * Interface to mark {@link Model models} which have an id
 * <p>
 * This type is package private because it is an implementation detail of the models and
 * z should not be used outside of this package.
 * </p>
 */
interface HasId {
  Long getId();

  void setId(Long id);
}
