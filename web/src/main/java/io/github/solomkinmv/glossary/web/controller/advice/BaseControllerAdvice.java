package io.github.solomkinmv.glossary.web.controller.advice;

import io.github.solomkinmv.glossary.service.exception.DomainObjectAlreadyExistException;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import io.github.solomkinmv.glossary.service.exception.ImageExistException;
import org.springframework.hateoas.VndErrors;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;

/**
 * Controller advice which intercepts exceptions and returns {@link VndErrors}.
 */
@RestControllerAdvice
public class BaseControllerAdvice {

    @ExceptionHandler(DomainObjectAlreadyExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    VndErrors entryAlreadyExist(DomainObjectAlreadyExistException e) {
        return new VndErrors("error", e.getMessage());
    }

    @ExceptionHandler(DomainObjectNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors domainObjectNotFound(DomainObjectNotFound e) {
        return new VndErrors("error", e.getMessage());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    VndErrors entityNotFound(EntityNotFoundException e) {
        return new VndErrors("error", e.getMessage());
    }

    @ExceptionHandler(ImageExistException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    VndErrors imageAlreadyExist(ImageExistException e) {
        return new VndErrors("error", e.getMessage());
    }
}
