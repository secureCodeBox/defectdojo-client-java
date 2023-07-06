// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
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
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)

public class UserProfile extends BaseModel {

    @JsonProperty
    User user;

    @Override
    public boolean equalsQueryString(Map<String, Object> queryParams) {
        // The user_profile endpoint does not have query parameters thats why this function will just return true
        return true;
    }
    
}
