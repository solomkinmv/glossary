package io.github.solomkinmv.glossary.storage.exception;

public class UploadException extends RuntimeException {

    public UploadException(String message) {
        super(message);
    }

    public UploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
