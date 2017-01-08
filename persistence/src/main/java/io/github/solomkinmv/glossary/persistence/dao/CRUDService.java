package io.github.solomkinmv.glossary.persistence.dao;

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

    T saveOrUpdate(T domainObject);

    void delete(K id);
}
