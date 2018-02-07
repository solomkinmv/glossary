package io.github.solomkinmv.glossary.storage.exception;

/**
 * Signals exception during storing an object.
 */
public class StorageException extends RuntimeException {
    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
