package io.securecodebox.persistence.defectdojo.http;

import lombok.Builder;
import lombok.Value;

/**
 * Holds HTTP proxy configuration
 * <p>
 * This class is immutable by design and therefor thread safe. As defaults it does not use |{@code null} to prevent null
 * pointer exceptions. It utilizes sane defaults (empty string or 0) to indicate a not set value. Also it introduces a
 * null-object to indicate a not-existing configuration.
 * </p>
 */
@Value
@Builder
class ProxyConfig {
    /**
     * Null pattern object.
     */
    static final ProxyConfig NULL = ProxyConfig.builder().build();
    private static final String DEFAULT_STRING = "";
    private static final int DEFAULT_INT = 0;

    /**
     * Username to authenticate on a proxy.
     * <p>
     * Defaults to empty string.
     * </p>
     */
    @Builder.Default
    String user = DEFAULT_STRING;

    /**
     * Password to authenticate on a proxy.
     * <p>
     * Defaults to empty string.
     * </p>
     */
    @Builder.Default
    String password = DEFAULT_STRING;

    /**
     * Host name of the proxy.
     * <p>
     * Defaults to empty string.
     * </p>
     */
    @Builder.Default
    String host = DEFAULT_STRING;

    /**
     * Port of the proxy.
     * <p>
     * Defaults to 0 (zero).
     * </p>
     */
    @Builder.Default
    int port = DEFAULT_INT;

    /**
     *  configuration is considered complete if all values are not default values
     *
     * @return {@code true} if all values are set else {@code false}
     */
    boolean isComplete() {
        if (getUser().equals(DEFAULT_STRING)) {
            return false;
        }

        if (getPassword().equals(DEFAULT_STRING)) {
            return false;
        }

        if (getHost().equals(DEFAULT_STRING)) {
            return false;
        }

        if (getPort() == DEFAULT_INT) {
            return false;
        }

        return true;
    }
}
