// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class ProductGroup implements Model {
    @JsonProperty
    private Long id;// FIXME: Use native type here.

    @JsonProperty
    private Long product;// FIXME: Use native type here.

    @JsonProperty
    private Long group;// FIXME: Use native type here.

    @JsonProperty
    private Long role;// FIXME: Use native type here.

    @Override
    public boolean equalsQueryString(Map<String, Object> queryParams) {
        if (queryParams.containsKey("id") && queryParams.get("id").equals(this.id)) {
            return true;
        }
        if (queryParams.containsKey("product") && queryParams.get("product").equals(this.product) && queryParams.containsKey("group") && queryParams.get("group").equals(this.group)) {
            return true;
        }
        return false;
    }
}
