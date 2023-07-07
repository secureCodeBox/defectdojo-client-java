// Copyright 2021 iteratec GmbH
// SPDX-FileCopyrightText: 2023 iteratec GmbH
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.Optional;

/**
 * Configures the DefectDojo client
 */
@Getter
@ToString
@AllArgsConstructor
public final class Config {
    /**
     * Default for {@link #maxPageCountForGets}
     */
    private static final int DEFAULT_MAX_PAGE_COUNT_FOR_GETS = 100;
    /**
     * URL of the host which serves the DefectDojo API.
     * <p>
     * It is only allowed to configure the base URL (e.g. {@literal "https://defectdojo.securecodebox.io/"} without
     * any path. The path to the concrete API endpoints are maintained by this client library itself.
     * </p>
     */
    private final String url;
    /**
     * API key to authorize against the DefectDojo API.
     */
    private final String apiKey;
    /**
     * This name is used to set the creator of entities created in DefectDojo (findings etc.).
     * <p>
     * Since DefectDojo requires the id of the user this client lib must do a lookup to determine the according id.
     * This does not work, if the user does nit have appropriate privileges. In this case you can set the {@link #userId}
     * directly with the appropriate id of the user you want as creator.
     * </p>
     *
     * @deprecated Must not be used anymore because we determine the userid via user_profile API endpoint.
     */
    @Deprecated
    private final String username;

    /**
     * How many pages of objects are fetched from the DefectDojo API
     * <p>
     * This setting is to avoid out of memory scenarios.
     * </p>
     * <p>
     * Defaults to {@link #DEFAULT_MAX_PAGE_COUNT_FOR_GETS}.
     * </p>
     */
    private final int maxPageCountForGets;

    /**
     * Overwrite the creator by userid
     * <p>
     * <strong>IMPORTANT</strong>: If this is set (not {@code null}) the {@link #username} is ignored!
     * </p>
     * <p>
     * This option is necessary, if the user belonging to the {@link #apiKey} has no privilege to determine it's userid.
     * </p>
     *
     * @deprecated Must not be used anymore because we determine the userid via user_profile API endpoint.
     */
    @Deprecated
    private final Long userId;

    /**
     * Default constructor which sets {@link #userId} to {@code null}
     *
     * @param url                 not {@code null}
     * @param apiKey              not {@code null}
     * @param username            not {@code null}
     * @param maxPageCountForGets not less than 1
     */
    public Config(final @NonNull String url, final @NonNull String apiKey, final @NonNull String username, final int maxPageCountForGets) {
        // FIXME: Implement check that maxPageCountForGets is not less than 1
        this(url, apiKey, username, maxPageCountForGets, null);
    }

    /**
     * Creates config from environment variables
     *
     * @return never {@code null}
     */
    public static Config fromEnv() {
        final var url = System.getenv("DEFECTDOJO_URL");
        final var username = System.getenv("DEFECTDOJO_USERNAME");
        final var apiKey = System.getenv("DEFECTDOJO_APIKEY");
        final var userId = Optional.ofNullable(System.getenv("DEFECTDOJO_USER_ID")).map(Long::parseLong).orElse(null);

        int maxPageCountForGets = DEFAULT_MAX_PAGE_COUNT_FOR_GETS;

        if (System.getenv("DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS") != null) {
            maxPageCountForGets = Integer.parseInt(System.getenv("DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS"));
        }

        return new Config(url, apiKey, username, maxPageCountForGets, userId);
    }
}
