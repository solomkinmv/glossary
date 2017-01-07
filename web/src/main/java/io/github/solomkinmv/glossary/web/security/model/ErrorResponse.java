package io.github.solomkinmv.glossary.web.security.model;

import io.github.solomkinmv.glossary.web.security.util.ErrorCode;
import org.springframework.http.HttpStatus;

import java.time.Instant;

/**
 * Represents error. Used to return error as formatted JSON.
 */
public class ErrorResponse {
    private final HttpStatus status;
    private final String message;
    private final ErrorCode errorCode;
    private final Instant timestamp;

    public ErrorResponse(String message, ErrorCode errorCode, HttpStatus status) {
        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.timestamp = Instant.now();
    }

    public static ErrorResponse of(String message, ErrorCode errorCode, HttpStatus status) {
        return new ErrorResponse(message, errorCode, status);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
