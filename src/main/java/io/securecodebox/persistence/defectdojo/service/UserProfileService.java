// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import io.securecodebox.persistence.defectdojo.config.Config;
import io.securecodebox.persistence.defectdojo.model.Response;
import io.securecodebox.persistence.defectdojo.model.UserProfile;

public class UserProfileService extends GenericDefectDojoService<UserProfile>{

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
    protected Response<UserProfile> deserializeList(String response) throws JsonProcessingException {
        // GenericDefectDojoService expects that the response from the defectdojo api is a list
        // This endpoint returns a single object though, to not break the code this response gets converted to a defectdojo response
        UserProfile userProfile = this.objectMapper.readValue(response, new TypeReference<>() {});
        List<UserProfile> userProfileList = new ArrayList<>();
        userProfileList.add(userProfile);

        Response<UserProfile> fakeResult = new Response<>();
        fakeResult.setResults(userProfileList);
        fakeResult.setCount(1);
        return fakeResult;
    }
}
