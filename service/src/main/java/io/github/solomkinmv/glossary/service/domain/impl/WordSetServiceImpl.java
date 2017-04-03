package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.UserDictionaryDao;
import io.github.solomkinmv.glossary.persistence.dao.WordSetDao;
import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link WordSetService}.
 */
@Service
@Transactional
@Slf4j
public class WordSetServiceImpl implements WordSetService {

    private final WordSetDao wordSetDao;
    private final UserDictionaryDao userDictionaryDao;

    @Autowired
    public WordSetServiceImpl(WordSetDao wordSetDao, UserDictionaryDao userDictionaryDao) {
        this.wordSetDao = wordSetDao;
        this.userDictionaryDao = userDictionaryDao;
    }

    @Override
    public List<WordSet> listAll() {
        log.debug("Listing all wordSets");
        return wordSetDao.listAll();
    }

    @Override
    public Optional<WordSet> getById(Long id) {
        log.debug("Getting wordSet by id: {}", id);
        return wordSetDao.findOne(id);
    }

    @Override
    public WordSet save(WordSet wordSet) {
        log.debug("Saving wordSet: {}", wordSet);

        wordSetDao.create(wordSet);

        return wordSet;
    }

    @Override
    public WordSet update(WordSet wordSet) {
        log.debug("Updating wordSet: {}", wordSet);
        Long wordSetId = wordSet.getId();
        if (wordSetId == null) {
            log.error("Can't update wordSet with null id");
            throw new DomainObjectNotFound("Can't update wordSet with null id");
        }

        Optional<WordSet> wordSetOptional = wordSetDao.findOne(wordSetId);
        if (!wordSetOptional.isPresent()) {
            throw new DomainObjectNotFound("No wordSet with id " + wordSetId);
        }

        return wordSetDao.update(wordSet);
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting wordSet with id: {}", id);
        wordSetDao.delete(id);
    }

    @Override
    public void deleteAll() {
        log.debug("Deleting all wordSets");
        wordSetDao.deleteAll();
    }

    @Override
    public List<WordSet> listByUsername(String username) {
        log.debug("Listing all wordSets by username {}", username);
        return wordSetDao.listByUsername(username);
    }

    @Override
    public Optional<WordSet> getByIdAndUsername(Long id, String username) {
        log.debug("Getting word set by id {} and username {}", id, username);
        return wordSetDao.findByIdAndUsername(id, username);
    }

    @Override
    public WordSet saveForUser(String username, WordSet wordSet) {
        UserDictionary userDictionary = userDictionaryDao
                .findByUsername(username)
                .orElseThrow(() -> new DomainObjectNotFound("Can't find UserDictionary by username: " + username));
        wordSet.setUserDictionary(userDictionary);
        wordSetDao.create(wordSet);

        log.debug("Updating user dictionary before saving word set: ", wordSet);
        if (userDictionary.getWordSets() == null) {
            userDictionary.setWordSets(new HashSet<>());
        }
        userDictionary.getWordSets().add(wordSet);
        userDictionaryDao.update(userDictionary);
        return wordSet;
    }

    @Override
    public void deleteByIdAndUsername(Long id, String username) {
        wordSetDao.deleteByIdAndUsername(id, username);
        /*WordSet wordSet = wordSetDao.findByIdAndUsername(id, username)
                                    .orElseThrow(() -> new DomainObjectNotFound(
                                            "Can't find WordSet by id " + id + "  and username: " + username));

        wordSetDao.delete(wordSet.getId());*/

    }
}
