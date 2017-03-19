package io.github.solomkinmv.glossary.service.exception;

/**
 * Signals exception during storing an image.
 */
public class ImageStoreException extends RuntimeException {
    public ImageStoreException(String message) {
        super(message);
    }

    public ImageStoreException(String message, Throwable cause) {
        super(message, cause);
    }
}
