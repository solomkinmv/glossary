package io.github.solomkinmv.glossary.image.exception;


/**
 * Represents exception during {@link ImageService} processing.
 */
public class ImageSearchException extends RuntimeException {

    public ImageSearchException(String message) {
        super(message);
    }

    public ImageSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
