package io.github.solomkinmv.glossary.words.exception;

public class DomainObjectNotFound extends RuntimeException {
    public DomainObjectNotFound(String message) {
        super(message);
    }

    public DomainObjectNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
