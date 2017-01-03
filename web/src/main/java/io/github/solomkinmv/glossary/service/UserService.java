package io.github.solomkinmv.glossary.service;

import io.github.solomkinmv.glossary.persistence.model.User;

import java.util.Optional;

/**
 * Created by max on 02.01.17.
 * TODO: add JavaDoc
 */
public interface UserService {
    Optional<User> getByUsername(String username);
}
