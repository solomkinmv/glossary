package io.github.solomkinmv.glossary.web.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Represents JWT expired exception
 */
public class JwtExpiredException extends AuthenticationException {

    private final String token;

    public JwtExpiredException(String msg, Throwable t, String token) {
        super(msg, t);
        this.token = token;
    }

    public JwtExpiredException(String msg, String token) {
        super(msg);
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
