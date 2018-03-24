package io.github.solomkinmv.glossary.tts.exception;

public class TtsServiceException extends RuntimeException {

    public TtsServiceException(String message) {
        super(message);
    }

    public TtsServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
