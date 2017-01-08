package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.UserDao;
import io.github.solomkinmv.glossary.persistence.model.User;
import io.github.solomkinmv.glossary.service.domain.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserService}.
 */
@Service
public class UserServiceImpl implements UserService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public List<User> listAll() {
        LOGGER.debug("Listing all users");
        return userDao.listAll();
    }

    @Override
    public Optional<User> getById(long id) {
        LOGGER.debug("Getting user by id: {}", id);
        return userDao.getById(id);
    }

    @Override
    public User saveOrUpdate(User user) {
        LOGGER.debug("Saving or updating user: {}", user);
        return userDao.saveOrUpdate(user);
    }

    @Override
    public void delete(long id) {
        LOGGER.debug("Deleting user with id: {}", id);
        userDao.delete(id);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        LOGGER.debug("Getting user by username: {}", username);
        return userDao.findByUsername(username);
    }
}
