package io.github.solomkinmv.glossary.words.exception;

public class DomainObjectNotFound extends WordsServiceException {
    public DomainObjectNotFound(String message) {
        super(message);
    }

    public DomainObjectNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
