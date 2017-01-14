package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.UserDictionaryDao;
import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import io.github.solomkinmv.glossary.service.domain.UserDictionaryService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectAlreadyExistException;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserDictionaryService}.
 */
@Service
public class UserDictionaryServiceImpl implements UserDictionaryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserDictionaryServiceImpl.class);

    private final UserDictionaryDao userDictionaryDao;

    @Autowired
    public UserDictionaryServiceImpl(UserDictionaryDao userDictionaryDao) {
        this.userDictionaryDao = userDictionaryDao;
    }

    @Override
    public List<UserDictionary> listAll() {
        LOGGER.debug("Listing all userDictionarys");
        return userDictionaryDao.listAll();
    }

    @Override
    public Optional<UserDictionary> getById(Long id) {
        LOGGER.debug("Getting userDictionary by id: {}", id);
        return userDictionaryDao.getById(id);
    }

    @Override
    public UserDictionary save(UserDictionary userDictionary) {
        LOGGER.debug("Saving userDictionary: {}", userDictionary);
        if (userDictionary.getId() != null) {
            return getById(userDictionary.getId())
                    .map(userDictionaryDao::saveOrUpdate)
                    .orElseThrow(() -> new DomainObjectAlreadyExistException(
                            "UserDictionary with such id is already exist: " + userDictionary.getId()));
        }

        return userDictionaryDao.saveOrUpdate(userDictionary);
    }

    @Override
    public UserDictionary update(UserDictionary userDictionary) {
        LOGGER.debug("Updating userDictionary: {}", userDictionary);
        if (userDictionary.getId() == null) {
            LOGGER.error("Can't update userDictionary with null id");
            throw new DomainObjectNotFound("Can't update userDictionary with null id");
        }
        return userDictionaryDao.saveOrUpdate(userDictionary);
    }

    @Override
    public void delete(Long id) {
        LOGGER.debug("Deleting userDictionary with id: {}", id);
        userDictionaryDao.delete(id);
    }

    @Override
    public Optional<UserDictionary> getByUsername(String username) {
        LOGGER.debug("Getting userDictionary by username: {}", username);
        return userDictionaryDao.findByUsername(username);
    }
}
