package io.github.solomkinmv.glossary.web.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * Represents exception which occurred during authentication
 */
public class AuthMethodNotSupportedException extends AuthenticationException {
    public AuthMethodNotSupportedException(String msg) {
        super(msg);
    }
}
