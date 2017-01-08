package io.github.solomkinmv.glossary.service.domain;

import io.github.solomkinmv.glossary.persistence.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Describes methods to interact with {@link User}.
 */
public interface UserService {
    List<User> listAll();

    Optional<User> getById(long id);

    User saveOrUpdate(User user);

    void delete(long id);

    Optional<User> getByUsername(String username);
}
