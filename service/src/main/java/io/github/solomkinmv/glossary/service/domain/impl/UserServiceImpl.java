package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.UserDao;
import io.github.solomkinmv.glossary.persistence.model.User;
import io.github.solomkinmv.glossary.service.domain.UserService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectAlreadyExistException;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
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
    public Optional<User> getById(Long id) {
        LOGGER.debug("Getting user by id: {}", id);
        return userDao.getById(id);
    }

    @Override
    public User save(User user) {
        LOGGER.debug("Saving user: {}", user);
        if (user.getId() != null) {
            return getById(user.getId())
                    .map(userDao::saveOrUpdate)
                    .orElseThrow(() -> new DomainObjectAlreadyExistException(
                            "User with such id is already exist: " + user.getId()));
        }

        return userDao.saveOrUpdate(user);
    }

    @Override
    public User update(User user) {
        LOGGER.debug("Updating user: {}", user);
        if (user.getId() == null) {
            LOGGER.error("Can't update user with null id");
            throw new DomainObjectNotFound("Can't update user with null id");
        }
        return userDao.saveOrUpdate(user);
    }

    @Override
    public void delete(Long id) {
        LOGGER.debug("Deleting user with id: {}", id);
        userDao.delete(id);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        LOGGER.debug("Getting user by username: {}", username);
        return userDao.findByUsername(username);
    }
}
