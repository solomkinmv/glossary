package io.github.solomkinmv.glossary.service.exception;

/**
 * Represents exception when domain object not found.
 */
public class DomainObjectNotFound extends RuntimeException {
    public DomainObjectNotFound(String message) {
        super(message);
    }

    public DomainObjectNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
