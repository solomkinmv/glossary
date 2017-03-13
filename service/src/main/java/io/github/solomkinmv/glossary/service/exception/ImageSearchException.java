package io.github.solomkinmv.glossary.service.exception;

import io.github.solomkinmv.glossary.service.flickr.ImageSearch;

/**
 * Represents exception during {@link ImageSearch} processing.
 */
public class ImageSearchException extends RuntimeException {

    public ImageSearchException(String message) {
        super(message);
    }

    public ImageSearchException(String message, Throwable cause) {
        super(message, cause);
    }
}
