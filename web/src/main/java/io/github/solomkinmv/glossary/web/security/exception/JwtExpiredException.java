package io.github.solomkinmv.glossary.web.security.exception;

import io.github.solomkinmv.glossary.web.security.model.token.Jwt;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
public class JwtExpiredException extends AuthenticationException {

    private final Jwt token;

    public JwtExpiredException(String msg, Throwable t,
                               Jwt token) {
        super(msg, t);
        this.token = token;
    }

    public JwtExpiredException(String msg, Jwt token) {
        super(msg);
        this.token = token;
    }

    public Jwt getToken() {
        return token;
    }
}
