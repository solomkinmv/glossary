package io.github.solomkinmv.glossary.words.controller.advice;

import io.github.solomkinmv.glossary.words.exception.DomainObjectNotFound;
import io.github.solomkinmv.glossary.words.exception.ExceptionMessage;
import io.github.solomkinmv.glossary.words.exception.WordsServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionHandlingAdvice {

    @ExceptionHandler(DomainObjectNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ExceptionMessage domainObjectNotFound(DomainObjectNotFound e) {
        return new ExceptionMessage(e.getMessage());
    }

    @ExceptionHandler({IllegalArgumentException.class, WordsServiceException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    ExceptionMessage badRequest(IllegalArgumentException e) {
        return new ExceptionMessage(e.getMessage());
    }
}
