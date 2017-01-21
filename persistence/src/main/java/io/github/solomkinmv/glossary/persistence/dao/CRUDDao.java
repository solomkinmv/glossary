package io.github.solomkinmv.glossary.persistence.dao;

import java.util.List;
import java.util.Optional;

/**
 * Interface for CRUD operations.
 *
 * @param <T> type of domain object
 * @param <K> type of key
 */
public interface CRUDDao<T, K> {
    List<T> listAll();

    Optional<T> findOne(K id);

    void create(T entity);

    T update(T entity);

    void delete(K id);

    void deleteAll();
}
