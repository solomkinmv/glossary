package io.github.solomkinmv.glossary.service.exception;

import io.github.solomkinmv.glossary.service.images.ImageService;

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
