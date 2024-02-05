// SPDX-FileCopyrightText: the secureCodeBox authors
//
// SPDX-License-Identifier: Apache-2.0

package io.securecodebox.persistence.defectdojo.config;

import io.securecodebox.persistence.defectdojo.exception.ConfigException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

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
   * Null pattern object.
   */
  public static final Config NULL = new Config("", "", DEFAULT_MAX_PAGE_COUNT_FOR_GETS);

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
   * Convenience constructor which sets {@link #DEFAULT_MAX_PAGE_COUNT_FOR_GETS}
   *
   * @param url    not {@code null}
   * @param apiKey not {@code null}
   */
  public Config(final @NonNull String url, final @NonNull String apiKey) {
    this(url, apiKey, DEFAULT_MAX_PAGE_COUNT_FOR_GETS);
  }

  /**
   * Dedicated constructor
   *
   * @param url                 not {@code null}
   * @param apiKey              not {@code null}
   * @param maxPageCountForGets not less than 1
   */
  public Config(final @NonNull String url, final @NonNull String apiKey, final int maxPageCountForGets) {
    super();
    this.url = url;
    this.apiKey = apiKey;
    this.maxPageCountForGets = validateIsGreaterZero(maxPageCountForGets, "maxPageCountForGets");
  }

  private static int validateIsGreaterZero(final int number, final String name) {
    if (number < 1) {
      throw new IllegalArgumentException(String.format("%s must be greater than 0!", name));
    }

    return number;
  }

  /**
   * Creates config from environment variables
   *
   * @return never {@code null}
   */
  public static Config fromEnv() {
    final var url = findRequiredEnvVar(EnvVars.DEFECTDOJO_URL);
    final var apiKey = findRequiredEnvVar(EnvVars.DEFECTDOJO_APIKEY);
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

    return new Config(url, apiKey, maxPageCountForGets);
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
    DEFECTDOJO_APIKEY("DEFECTDOJO_APIKEY"),
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
