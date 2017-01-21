package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordSetDao;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link WordSetService}.
 */
@Service
@Transactional
public class WordSetServiceImpl implements WordSetService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordSetServiceImpl.class);

    private final WordSetDao wordSetDao;

    @Autowired
    public WordSetServiceImpl(WordSetDao wordSetDao) {
        this.wordSetDao = wordSetDao;
    }

    @Override
    public List<WordSet> listAll() {
        LOGGER.debug("Listing all wordSets");
        return wordSetDao.listAll();
    }

    @Override
    public Optional<WordSet> getById(Long id) {
        LOGGER.debug("Getting wordSet by id: {}", id);
        return wordSetDao.findOne(id);
    }

    @Override
    public WordSet save(WordSet wordSet) {
        LOGGER.debug("Saving wordSet: {}", wordSet);

        wordSetDao.create(wordSet);

        return wordSet;
    }

    @Override
    public WordSet update(WordSet wordSet) {
        LOGGER.debug("Updating wordSet: {}", wordSet);
        Long wordSetId = wordSet.getId();
        if (wordSetId == null) {
            LOGGER.error("Can't update wordSet with null id");
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
        LOGGER.debug("Deleting wordSet with id: {}", id);
        wordSetDao.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("Deleting all wordSets");
        wordSetDao.deleteAll();
    }
}
