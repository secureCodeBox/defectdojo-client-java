// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Group extends BaseModel {
    @JsonProperty
    Long id;

    @JsonProperty
    @NonNull
    String name;

    @JsonProperty
    String description;

    @JsonProperty
    List<Long> users;

    @JsonProperty("social_provider")
    String socialProvider;

    @Override
    public boolean equalsQueryString(Map<String, Object> queryParams) {
        if (queryParams.containsKey("id") && queryParams.get("id").equals(this.id)) {
            return true;
        }
        if (queryParams.containsKey("name") && queryParams.get("name").equals(this.name)) {
            return true;
        }
        return false;
    }
}
