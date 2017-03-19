package io.github.solomkinmv.glossary.service.exception;

/**
 * Signals exception when image with such name already exists.
 */
public class ImageExistException extends ImageStoreException {
    public ImageExistException(String message) {
        super(message);
    }

    public ImageExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
