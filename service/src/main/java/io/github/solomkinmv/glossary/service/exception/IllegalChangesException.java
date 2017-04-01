package io.github.solomkinmv.glossary.service.exception;

public class IllegalChangesException extends RuntimeException {
    public IllegalChangesException(String message) {
        super(message);
    }

    public IllegalChangesException(String message, Throwable cause) {
        super(message, cause);
    }
}
