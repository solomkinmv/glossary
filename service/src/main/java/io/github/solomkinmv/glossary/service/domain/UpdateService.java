package io.github.solomkinmv.glossary.service.domain;

/**
 * Created by max on 4/1/17.
 */
public interface UpdateService<T> {
    T update(T domainObject);
}
