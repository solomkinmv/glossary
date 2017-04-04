package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.StudiedWordDao;
import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import io.github.solomkinmv.glossary.service.speach.SpeechService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link WordService}.
 */
@Service
@Transactional
@Slf4j
public class WordServiceImpl implements WordService {
    private final StudiedWordDao studiedWordDao;
    private final WordDao wordDao;
    private final SpeechService speechService;

    @Autowired
    public WordServiceImpl(StudiedWordDao studiedWordDao, WordDao wordDao, SpeechService speechService) {
        this.studiedWordDao = studiedWordDao;
        this.wordDao = wordDao;
        this.speechService = speechService;
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

        Word associatedWord = getSynchronizedAssociatedWord(studiedWord);

        studiedWord.setSound(associatedWord.getSound());
        studiedWordDao.create(studiedWord);

        return studiedWord;
    }

    @Override
    public void delete(Long id) {
        log.debug("Deleting studiedWord with id: {}", id);
        StudiedWord studiedWord = getById(id)
                .orElseThrow(
                        () -> new DomainObjectNotFound("Can't get studied word with id " + id));
        synchronizeAssociatedWordOnDelete(studiedWord);
        studiedWordDao.delete(id);
    }

    @Override
    public void deleteAll() {
        log.debug("Deleting all words");
        studiedWordDao.deleteAll();
        wordDao.deleteAll();
    }

    private void synchronizeAssociatedWordOnDelete(StudiedWord studiedWord) {
        wordDao.findByText(studiedWord.getText()).ifPresent(word -> {
            if (word.getTranslations().size() == 1) {
                wordDao.delete(word.getId());
            } else {
                word.getTranslations().remove(studiedWord.getTranslation());
                word.getImages().remove(studiedWord.getImage());
                wordDao.update(word);
            }
        });
    }

    private Word getSynchronizedAssociatedWord(StudiedWord studiedWord) {
        Optional<Word> wordOptional = wordDao.findByText(studiedWord.getText());
        if (wordOptional.isPresent()) {
            Word word = wordOptional.get();
            List<String> translations = word.getTranslations();
            if (!translations.contains(studiedWord.getTranslation())) {
                translations.add(studiedWord.getTranslation());
            }
            List<String> images = word.getImages();
            if (!images.contains(studiedWord.getImage())) {
                word.getImages().add(studiedWord.getImage());
            }
            wordDao.update(word);
            return word;
        } else {
            return createInitialWord(studiedWord);
        }
    }

    private Word createInitialWord(StudiedWord studiedWord) {

        List<String> translations = new ArrayList<>();
        translations.add(studiedWord.getTranslation());
        List<String> images = new ArrayList<>();
        if (studiedWord.getImage() != null) {
            images.add(studiedWord.getImage());
        }
        Word word = new Word(studiedWord.getText(), translations, images,
                             speechService.getSpeechRecord(studiedWord.getText()));
        wordDao.create(word);
        return word;
    }

    @Override
    public List<StudiedWord> listByUsername(String username) {
        log.debug("Listing all by username: {}", username);
        return studiedWordDao.listAllByUsername(username);
    }

    @Override
    public List<Word> search(String query) {
        log.debug("Searching for words by following query: {}", query);
        return wordDao.search(query);
    }

    @Override
    public Optional<Word> findByText(String text) {
        log.debug("Looking for words by text {}", text);
        return wordDao.findByText(text);
    }

    @Override
    public Optional<StudiedWord> getWordByIdAndUsername(Long wordId, String username) {
        return studiedWordDao.findByIdAndUsername(wordId, username);
    }
}
