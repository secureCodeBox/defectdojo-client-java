// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

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
    private final Long userId;

    public DefectDojoConfig(String url, String apiKey, String username, int maxPageCountForGets) {
        this(url, apiKey, username, maxPageCountForGets, null);
    }

    public static DefectDojoConfig fromEnv() {
        String url = System.getenv("DEFECTDOJO_URL");
        String username = System.getenv("DEFECTDOJO_USERNAME");
        String apiKey = System.getenv("DEFECTDOJO_APIKEY");
        Long userId = Optional.ofNullable(System.getenv("DEFECTDOJO_USER_ID")).map(Long::parseLong).orElse(null);

        int maxPageCountForGets = 100;
        if (System.getenv("DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS") != null) {
            maxPageCountForGets = Integer.parseInt(System.getenv("DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS"));
        }
        return new DefectDojoConfig(url, apiKey, username, maxPageCountForGets, userId);
    }
}
