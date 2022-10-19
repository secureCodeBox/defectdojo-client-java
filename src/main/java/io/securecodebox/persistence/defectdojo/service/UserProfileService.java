/*
 *  secureCodeBox (SCB)
 *  Copyright 2021 iteratec GmbH
 *  https://www.iteratec.com
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package io.securecodebox.persistence.defectdojo.service;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;

import io.securecodebox.persistence.defectdojo.config.DefectDojoConfig;
import io.securecodebox.persistence.defectdojo.models.DefectDojoResponse;
import io.securecodebox.persistence.defectdojo.models.UserProfile;

public class UserProfileService extends GenericDefectDojoService<UserProfile>{

    public UserProfileService(DefectDojoConfig config) {
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
    protected DefectDojoResponse<UserProfile> deserializeList(String response) throws JsonProcessingException {
        // GenericDefectDojoService expects that the response from the defectdojo api is a list
        // This endpoint returns a single object though, to not break the code this response gets converted to a defectdojo response
        UserProfile userProfile = this.objectMapper.readValue(response, new TypeReference<>() {});
        List<UserProfile> userProfileList = new ArrayList<>();
        userProfileList.add(userProfile);

        DefectDojoResponse<UserProfile> fakeResult = new DefectDojoResponse<>();
        fakeResult.setResults(userProfileList);
        fakeResult.setCount(1);
        return fakeResult;
    }
}
