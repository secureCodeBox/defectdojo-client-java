// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DojoGroupMember extends DefectDojoModel {
    @JsonProperty
    Long id;

    @JsonProperty("group_id")
    Long group;

    @JsonProperty("user_id")
    Long user;

    @JsonProperty
    Long role;

    @Override
    public boolean equalsQueryString(Map<String, Object> queryParams) {
        if (queryParams.containsKey("id") && queryParams.get("id").equals(this.id)) {
            return true;
        }
        if (queryParams.containsKey("group") && queryParams.get("group").equals(this.group)) {
            return true;
        }
        return false;
    }
}
