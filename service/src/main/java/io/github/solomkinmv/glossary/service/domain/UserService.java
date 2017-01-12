package io.github.solomkinmv.glossary.service.domain;

import io.github.solomkinmv.glossary.persistence.model.User;

import java.util.Optional;

/**
 * Describes methods to interact with {@link User}.
 */
public interface UserService extends CRUDService<User, Long> {

    Optional<User> getByUsername(String username);
}
