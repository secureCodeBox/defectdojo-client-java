// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.config;

import io.securecodebox.persistence.defectdojo.exception.ConfigException;
import lombok.*;

import java.util.Optional;

/**
 * Configures the DefectDojo client
 */
@Getter
@ToString
@EqualsAndHashCode
public final class Config {
    /**
     * Default for {@link #maxPageCountForGets}
     */
    static final int DEFAULT_MAX_PAGE_COUNT_FOR_GETS = 100;
    /**
     * URL of the host which serves the DefectDojo API.
     * <p>
     * It is only allowed to configure the base URL (e.g. {@literal "https://defectdojo.securecodebox.io/"} without
     * any path. The path to the concrete API endpoints are maintained by this client library itself.
     * </p>
     */
    @NonNull
    private final String url;
    /**
     * API key to authorize against the DefectDojo API.
     */
    @NonNull
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
    @NonNull
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
     * Dedicated constructor
     *
     * @param url                 not {@code null}
     * @param apiKey              not {@code null}
     * @param username            not {@code null}
     * @param maxPageCountForGets not less than 1
     * @param userId              may be {@code null} (see {@link #userId})
     */
    public Config(final @NonNull String url, final @NonNull String apiKey, final @NonNull String username, final int maxPageCountForGets, final Long userId) {
        super();
        this.url = url;
        this.apiKey = apiKey;
        this.username = username;
        this.maxPageCountForGets = validateIsGreaterZero(maxPageCountForGets, "maxPageCountForGets");
        this.userId = userId;
    }

    private static int validateIsGreaterZero(final int number, final String name) {
        if (number < 1) {
            throw new IllegalArgumentException(String.format("%s must be greater than 0!", name));
        }

        return number;
    }

    /**
     * Default constructor which sets {@link #userId} to {@code null}
     *
     * @param url                 not {@code null}
     * @param apiKey              not {@code null}
     * @param username            not {@code null}
     * @param maxPageCountForGets not less than 1
     */
    public Config(final String url, final String apiKey, final String username, final int maxPageCountForGets) {
        this(url, apiKey, username, maxPageCountForGets, null);
    }

    /**
     * Creates config from environment variables
     *
     * @return never {@code null}
     */
    public static Config fromEnv() {
        final var url = findRequiredEnvVar(EnvVars.DEFECTDOJO_URL);
        final var username = findRequiredEnvVar(EnvVars.DEFECTDOJO_USERNAME);
        final var apiKey = findRequiredEnvVar(EnvVars.DEFECTDOJO_APIKEY);
        final Long userId;

        try {
            userId = Optional.ofNullable(findEnvVar(EnvVars.DEFECTDOJO_USER_ID))
                .map(Long::parseLong).orElse(null);
        } catch (final NumberFormatException e) {
            throw new ConfigException(
                String.format("Given user id for environment variable '%s' is not a valid id! Given was '%s'.", EnvVars.DEFECTDOJO_USER_ID.literal, findEnvVar(EnvVars.DEFECTDOJO_USER_ID)),
                e);
        }

        final int maxPageCountForGets;

        if (hasEnvVar(EnvVars.DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS)) {
            try {
                maxPageCountForGets = Integer.parseInt(findEnvVar(EnvVars.DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS));
            } catch (final NumberFormatException e) {
                throw new ConfigException(String.format("Given value for environment variable '%s' is not a valid number! Given was '%s'.", EnvVars.DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS.literal, findEnvVar(EnvVars.DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS)),
                    e);
            }
        } else {
            maxPageCountForGets = DEFAULT_MAX_PAGE_COUNT_FOR_GETS;
        }

        return new Config(url, apiKey, username, maxPageCountForGets, userId);
    }

    private static boolean hasEnvVar(final @NonNull EnvVars name) {
        return findEnvVar(name) != null;
    }

    private static String findEnvVar(final @NonNull EnvVars name) {
        return System.getenv(name.literal);
    }

    private static String findRequiredEnvVar(final @NonNull EnvVars name) {
        final var envVar = System.getenv(name.literal);

        if (envVar == null) {
            throw new ConfigException(String.format("Missing environment variable '%s'!", name.literal));
        }
        return envVar;
    }

    /**
     * Enumerates the available environment variables to configure the client
     */
    public enum EnvVars {
        DEFECTDOJO_URL("DEFECTDOJO_URL"),
        DEFECTDOJO_USERNAME("DEFECTDOJO_USERNAME"),
        DEFECTDOJO_APIKEY("DEFECTDOJO_APIKEY"),
        DEFECTDOJO_USER_ID("DEFECTDOJO_USER_ID"),
        DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS("DEFECTDOJO_MAX_PAGE_COUNT_FOR_GETS");
        /**
         * Literal name of configuration environment name
         * <p>
         * We use an own value to hold the actual value, so that refactoring the enum name does
         * not break the published API of env var names.
         * </p>
         */
        private final String literal;

        EnvVars(final String literal) {
            this.literal = literal;
        }
    }
}
