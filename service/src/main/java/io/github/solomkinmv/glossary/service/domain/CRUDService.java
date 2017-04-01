package io.github.solomkinmv.glossary.service.domain;

/**
 * Interface for CRUD operations.
 *
 * @param <T> type of domain object
 * @param <K> type of key
 */
public interface CRUDService<T, K> extends ReadService<T, K>, DeleteService<K>, CreateService<T>, UpdateService<T> {

}