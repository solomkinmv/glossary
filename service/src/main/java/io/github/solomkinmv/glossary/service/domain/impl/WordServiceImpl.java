package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.service.domain.WordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link WordService}.
 */
@Service
public class WordServiceImpl implements WordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordServiceImpl.class);

    private final WordDao wordDao;

    @Autowired
    public WordServiceImpl(WordDao wordDao) {
        this.wordDao = wordDao;
    }

    @Override
    public List<Word> listAll() {
        LOGGER.debug("Listing all words");
        return wordDao.listAll();
    }

    @Override
    public Optional<Word> getById(Long id) {
        LOGGER.debug("Getting word by id: {}", id);
        return wordDao.getById(id);
    }

    @Override
    public Word saveOrUpdate(Word word) {
        LOGGER.debug("Saving or updating word: {}", word);
        return wordDao.saveOrUpdate(word);
    }

    @Override
    public void delete(Long id) {
        LOGGER.debug("Deleting word with id: {}", id);
        wordDao.delete(id);
    }
}
