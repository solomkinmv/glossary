package io.github.solomkinmv.glossary.persistence.util;

import javax.persistence.NoResultException;

@FunctionalInterface
public interface DaoRetriever<T> {
    T retrieve() throws NoResultException;
}