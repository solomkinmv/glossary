package io.github.solomkinmv.glossary.persistence.dao;

import io.github.solomkinmv.glossary.persistence.model.User;

import java.util.Optional;

/**
 * Describes methods to interact with {@link User} domain object.
 */
public interface UserDao extends CRUDService<User, Long> {

    Optional<User> findByUsername(String username);
}
