package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.StudiedWordDao;
import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.service.domain.StudiedWordService;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class StudiedWordServiceImpl implements StudiedWordService {
    private final StudiedWordDao studiedWordDao;

    @Autowired
    public StudiedWordServiceImpl(StudiedWordDao studiedWordDao) {
        this.studiedWordDao = studiedWordDao;
    }

    @Override
    public List<StudiedWord> listAll() {
        log.debug("Listing all words");
        return studiedWordDao.listAll();
    }

    @Override
    public Optional<StudiedWord> getById(Long id) {
        log.debug("Getting studiedWord by id: {}", id);
        return studiedWordDao.findOne(id);
    }

    @Override
    public StudiedWord save(StudiedWord studiedWord) {
        log.debug("Saving studiedWord: {}", studiedWord);

        studiedWordDao.create(studiedWord);

        return studiedWord;
    }

    @Override
    public StudiedWord update(StudiedWord studiedWord) {
        log.debug("Updating studiedWord: {}", studiedWord);
        Long wordId = studiedWord.getId();
        if (wordId == null) {
            log.error("Can't update studiedWord with null id");
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
        log.debug("Deleting studiedWord with id: {}", id);
        studiedWordDao.delete(id);
    }

    @Override
    public void deleteAll() {
        log.debug("Deleting all words");
        studiedWordDao.deleteAll();
    }

    @Override
    public List<StudiedWord> listByUsername(String username) {
        log.debug("Listing all by username: {}", username);
        return studiedWordDao.listAllByUsername(username);
    }
}
