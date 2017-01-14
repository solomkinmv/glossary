package io.github.solomkinmv.glossary.web.exception;

/**
 * Represents exception when the entry is already exist.
 */
public class EntryAlreadyExistException extends RuntimeException {
    public EntryAlreadyExistException(String message) {
        super(message);
    }

    public EntryAlreadyExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
