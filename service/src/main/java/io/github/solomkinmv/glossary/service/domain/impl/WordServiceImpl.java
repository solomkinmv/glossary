package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link WordService}.
 */
@Service
@Transactional
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
        return wordDao.findOne(id);
    }

    @Override
    public Word save(Word word) {
        LOGGER.debug("Saving word: {}", word);

        wordDao.create(word);

        return word;
    }

    @Override
    public Word update(Word word) {
        LOGGER.debug("Updating word: {}", word);
        Long wordId = word.getId();
        if (wordId == null) {
            LOGGER.error("Can't update word with null id");
            throw new DomainObjectNotFound("Can't update word with null id");
        }

        Optional<Word> wordOptional = wordDao.findOne(wordId);
        if (!wordOptional.isPresent()) {
            throw new DomainObjectNotFound("No word with id " + wordId);
        }
        return wordDao.update(word);
    }

    @Override
    public void delete(Long id) {
        LOGGER.debug("Deleting word with id: {}", id);
        wordDao.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("Deleting all words");
        wordDao.deleteAll();
    }
}
