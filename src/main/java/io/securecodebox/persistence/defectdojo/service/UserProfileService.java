// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.PaginatedResult;
import io.securecodebox.persistence.defectdojo.model.UserProfile;
import lombok.NonNull;

import java.util.List;

public final class UserProfileService extends GenericDefectDojoService<UserProfile> {

  public UserProfileService(Config config) {
    super(config);
  }

  @Override
  protected String getUrlPath() {
    return "user_profile";
  }

  @Override
  protected Class<UserProfile> getModelClass() {
    return UserProfile.class;
  }

  @Override
  protected PaginatedResult<UserProfile> deserializeList(@NonNull String response) throws JsonProcessingException {
    /* GenericDefectDojoService expects that the response from the defectdojo api is a list.
     * This endpoint returns a single object though, to not break the code this response
     * gets converted to a defectdojo response.
     */
    final var userProfile = this.objectMapper.readValue(response, new TypeReference<UserProfile>() {
    });
    final var result = new PaginatedResult<UserProfile>();
    result.setResults(List.of(userProfile));
    result.setCount(1);
    return result;
  }
}
