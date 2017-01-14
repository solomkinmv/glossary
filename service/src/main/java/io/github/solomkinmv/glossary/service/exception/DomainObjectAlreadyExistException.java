package io.github.solomkinmv.glossary.service.exception;

/**
 * Represents exception when domain object with such id is already exist.
 */
public class DomainObjectAlreadyExistException extends RuntimeException {
    public DomainObjectAlreadyExistException(String message) {
        super(message);
    }

    public DomainObjectAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
