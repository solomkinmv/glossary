package io.github.solomkinmv.glossary.words.service.wordset;

import io.github.solomkinmv.glossary.words.exception.DomainObjectNotFound;
import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import io.github.solomkinmv.glossary.words.persistence.repository.WordSetRepository;
import io.github.solomkinmv.glossary.words.service.external.TtsFacade;
import io.github.solomkinmv.glossary.words.service.word.WordMeta;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class WordSetService {

    private final WordSetRepository wordSetRepository;
    private final TtsFacade ttsFacade;

    public List<WordSet> findAllForSubjectId(String subjectId) {
        return wordSetRepository.findAllBySubjectId(subjectId);
    }

    public long create(WordSetMeta wordSetMeta, String subjectId) {
        WordSet wordSet = new WordSet(subjectId, wordSetMeta.getName(), wordSetMeta.getDescription());
        return wordSetRepository.save(wordSet).getId();
    }

    @Transactional
    public WordSet addWordToWordSet(long wordSetId, WordMeta wordMeta) {
        Word unsavedWord = Word.builder()
                               .text(wordMeta.getText())
                               .translation(wordMeta.getTranslation())
                               .image(wordMeta.getImage())
                               .sound(ttsFacade.getSpeechUrl(wordMeta.getText()))
                               .stage(WordStage.NOT_LEARNED)
                               .build();
        WordSet wordSet = getWordSet(wordSetId);

        wordSet.getWords().add(unsavedWord);
        return wordSet;
    }

    public WordSet getWordSet(long wordSetId) {
        return findWordSet(wordSetId)
                .orElseThrow(() -> new DomainObjectNotFound(
                        "Can't find word set with id " + wordSetId));
    }

    public Optional<WordSet> findWordSet(long wordSetId) {
        return wordSetRepository.findById(wordSetId);
    }

    public void deleteWordSetById(long wordSetId) {
        wordSetRepository.deleteById(wordSetId);
    }

    @Transactional
    public WordSet updateWordSetMetaInformation(long wordSetId, WordSetMeta wordSetMeta) {
        WordSet wordSet = getWordSet(wordSetId);
        wordSet.setName(wordSetMeta.getName());
        wordSet.setDescription(wordSetMeta.getDescription());
        return wordSet;
    }

    @Transactional
    public void deleteWordFromWordSet(long wordSetId, long wordId) {
        getWordSet(wordSetId).getWords().removeIf(word -> word.getId() == wordId);
    }
}
