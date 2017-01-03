package io.github.solomkinmv.glossary.web.security.util;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
public enum ErrorCode {
    AUTHENTICATION, JWT_TOKEN_EXPIRED;

    @JsonValue
    public int getErrorCode() {
        return ordinal();
    }
}
