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
package io.securecodebox.persistence.defectdojo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
public class DefectDojoConfig {
    @Getter
    private final String url;

    @Getter
    private final String apiKey;

    @Getter
    private final String username;

    /**
     * Determines how many apiPages of Objects are fetched before giving up and failing to avoid outOfMemory scenarios.
     */
    @Getter
    private final int maxPageCountForGets;

    /**
     * If not null, the id should be used instead of the username.
     */
    @Getter
    private final Integer userId;

    public DefectDojoConfig(String url, String apiKey, String username, int maxPageCountForGets) {
        this(url,apiKey,username, maxPageCountForGets, null);
    }

    public static DefectDojoConfig fromEnv(){
        String url = System.getenv("DEFECTDOJO_URL");
        String username = System.getenv("DEFECTDOJO_USERNAME");
        String apiKey = System.getenv("DEFECTDOJO_APIKEY");
        Integer userId = Optional.ofNullable(System.getenv("DEFECTDOJO_USER_ID"))
                .map(Integer::parseInt)
                .orElse(null);

        int maxPageCountForGets = 100;
        if (System.getenv("DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS") != null) {
            maxPageCountForGets = Integer.parseInt(System.getenv("DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS"));
        }
        return new DefectDojoConfig(url, apiKey, username, maxPageCountForGets, userId);
    }
}
