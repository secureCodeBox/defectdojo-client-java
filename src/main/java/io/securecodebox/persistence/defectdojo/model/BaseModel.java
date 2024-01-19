// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode // FIXME: Implement hashCode/equals in inheritance is problematic https://www.artima.com/articles/how-to-write-an-equality-method-in-java (see https://github.com/secureCodeBox/defectdojo-client-java/issues/23)
abstract class BaseModel implements Model {
  // Class can be removed we remove @EqualsAndHashCode.
}
