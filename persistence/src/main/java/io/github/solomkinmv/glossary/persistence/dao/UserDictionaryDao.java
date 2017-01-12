package io.github.solomkinmv.glossary.persistence.dao;

import io.github.solomkinmv.glossary.persistence.model.UserDictionary;

import java.util.Optional;

/**
 * Describes methods to interact with {@link UserDictionary} domain object.
 */
public interface UserDictionaryDao extends CRUDService<UserDictionary, Long> {

    Optional<UserDictionary> findByUsername(String username);
}
