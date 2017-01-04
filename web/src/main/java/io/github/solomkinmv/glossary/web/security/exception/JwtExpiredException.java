package io.github.solomkinmv.glossary.web.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
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
