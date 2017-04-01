package io.github.solomkinmv.glossary.service.domain;

/**
 * Created by max on 4/1/17.
 */
public interface CreateService<T> {
    T save(T domainObject);
}
