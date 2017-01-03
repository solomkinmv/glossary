package io.github.solomkinmv.glossary.web.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
public class AuthMethodNotSupportedException extends AuthenticationException {
    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
