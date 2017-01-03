package io.github.solomkinmv.glossary.web.security.exception;

import io.github.solomkinmv.glossary.web.security.model.token.JwtToken;
import org.springframework.security.core.AuthenticationException;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
public class JwtExpiredException extends AuthenticationException {

    private final JwtToken token;

    public JwtExpiredException(String msg, Throwable t,
                               JwtToken token) {
        super(msg, t);
        this.token = token;
    }

    public JwtExpiredException(String msg, JwtToken token) {
        super(msg);
        this.token = token;
    }

    public JwtToken getToken() {
        return token;
    }
}
