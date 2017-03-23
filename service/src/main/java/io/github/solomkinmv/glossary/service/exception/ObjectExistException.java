package io.github.solomkinmv.glossary.service.exception;

/**
 * Signals exception when image with such name already exists.
 */
public class ObjectExistException extends StorageException {
    public ObjectExistException(String message) {
        super(message);
    }

    public ObjectExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
