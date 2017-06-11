package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.StudiedWordDao;
import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.persistence.model.WordStage;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.speach.SpeechService;
import io.github.solomkinmv.glossary.service.translate.Language;
import io.github.solomkinmv.glossary.service.translate.Translator;
import io.github.solomkinmv.glossary.service.translate.TranslatorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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
    private final Translator translator;

    @Autowired
    public WordServiceImpl(StudiedWordDao studiedWordDao, WordDao wordDao, SpeechService speechService, Translator translator) {
        this.studiedWordDao = studiedWordDao;
        this.wordDao = wordDao;
        this.speechService = speechService;
        this.translator = translator;
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

        studiedWord.setStage(WordStage.NOT_LEARNED);
        studiedWord.setSound(associatedWord.getSound());
        studiedWordDao.create(studiedWord);

        return studiedWord;
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
        wordDao.deleteAll();
    }

    private Word getSynchronizedAssociatedWord(StudiedWord studiedWord) {
        Optional<Word> wordOptional = wordDao.findByText(studiedWord.getText());
        if (wordOptional.isPresent()) {
            Word word = wordOptional.get();
            Set<String> translations = word.getTranslations();
            if (!translations.contains(studiedWord.getTranslation())) {
                translations.add(studiedWord.getTranslation());
            }
            Set<String> images = word.getImages();
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

        Set<String> translations = new HashSet<>();
        translations.add(studiedWord.getTranslation());
        try {
            translator.execute(studiedWord.getText(), Language.ENGLISH, Language.RUSSIAN)
                      .ifPresent(translations::add);
        } catch (TranslatorException e) {
            log.warn("Error occurred while using translator service", e);
        }

        Set<String> images = new HashSet<>();
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

    @Override
    public void updateWordMeta(StudiedWord word, WordStage stage, String image) {
        if (stage != null) {
            word.setStage(stage);
        }

        if (image != null) {
            word.setImage(image);
        }
        studiedWordDao.update(word);
    }
}
