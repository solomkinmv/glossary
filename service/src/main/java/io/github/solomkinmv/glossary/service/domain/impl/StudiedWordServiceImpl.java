package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.StudiedWordDao;
import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.service.domain.StudiedWordService;
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
public class StudiedWordServiceImpl implements StudiedWordService {
    private static final Logger LOGGER = LoggerFactory.getLogger(StudiedWordServiceImpl.class);

    private final StudiedWordDao studiedWordDao;

    @Autowired
    public StudiedWordServiceImpl(StudiedWordDao studiedWordDao) {
        this.studiedWordDao = studiedWordDao;
    }

    @Override
    public List<StudiedWord> listAll() {
        LOGGER.debug("Listing all words");
        return studiedWordDao.listAll();
    }

    @Override
    public Optional<StudiedWord> getById(Long id) {
        LOGGER.debug("Getting studiedWord by id: {}", id);
        return studiedWordDao.findOne(id);
    }

    @Override
    public StudiedWord save(StudiedWord studiedWord) {
        LOGGER.debug("Saving studiedWord: {}", studiedWord);

        studiedWordDao.create(studiedWord);

        return studiedWord;
    }

    @Override
    public StudiedWord update(StudiedWord studiedWord) {
        LOGGER.debug("Updating studiedWord: {}", studiedWord);
        Long wordId = studiedWord.getId();
        if (wordId == null) {
            LOGGER.error("Can't update studiedWord with null id");
            throw new DomainObjectNotFound("Can't update studiedWord with null id");
        }

        Optional<StudiedWord> wordOptional = studiedWordDao.findOne(wordId);
        if (!wordOptional.isPresent()) {
            throw new DomainObjectNotFound("No studiedWord with id " + wordId);
        }
        return studiedWordDao.update(studiedWord);
    }

    @Override
    public void delete(Long id) {
        LOGGER.debug("Deleting studiedWord with id: {}", id);
        studiedWordDao.delete(id);
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("Deleting all words");
        studiedWordDao.deleteAll();
    }
}
