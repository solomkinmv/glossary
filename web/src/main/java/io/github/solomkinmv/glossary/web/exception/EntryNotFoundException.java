package io.github.solomkinmv.glossary.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Represents exception when there is no such entry.
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntryNotFoundException extends RuntimeException {
    public EntryNotFoundException(String message) {
        super(message);
    }
}
