package io.github.solomkinmv.glossary.service.domain;

import java.util.List;
import java.util.Optional;

/**
 * Interface for CRUD operations.
 *
 * @param <T> type of domain object
 * @param <K> type of key
 */
public interface CRUDService<T, K> {
    List<T> listAll();

    Optional<T> getById(K id);

    T save(T domainObject);

    T update(T domainObject);

    void delete(K id);

    void deleteAll();
}