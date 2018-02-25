package io.github.solomkinmv.translateservice.exception;

/**
 * Signals that exception happened in Translator
 */
public class TranslatorException extends RuntimeException {

    public TranslatorException(String message) {
        super(message);
    }

    public TranslatorException(String message, Throwable cause) {
        super(message, cause);
    }
}
