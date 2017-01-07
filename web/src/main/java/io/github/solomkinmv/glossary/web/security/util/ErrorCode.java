package io.github.solomkinmv.glossary.web.security.util;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Contains enumeration of all error codes.
 */
public enum ErrorCode {
    AUTHENTICATION, JWT_TOKEN_EXPIRED;

    @JsonValue
    public int getErrorCode() {
        return ordinal();
    }
}
