package io.github.solomkinmv.glossary.words.exception;

public class WordsServiceException extends RuntimeException {

    public WordsServiceException(String message) {
        super(message);
    }

    public WordsServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
