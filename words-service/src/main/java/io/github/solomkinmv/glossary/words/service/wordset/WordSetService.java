package io.github.solomkinmv.glossary.words.service.wordset;

import io.github.solomkinmv.glossary.words.exception.DomainObjectNotFound;
import io.github.solomkinmv.glossary.words.persistence.domain.StudiedWord;
import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import io.github.solomkinmv.glossary.words.persistence.repository.WordSetRepository;
import io.github.solomkinmv.glossary.words.service.word.WordMeta;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class WordSetService {

    private final WordSetRepository wordSetRepository;

    public List<WordSet> findAllForUserId(long userId) {
        return wordSetRepository.findAllByUserId(userId);
    }

    public long create(WordSetMeta wordSetMeta) {
        WordSet wordSet = new WordSet(wordSetMeta.getUserId(), wordSetMeta.getName(), wordSetMeta.getDescription());
        return wordSetRepository.save(wordSet).getId();
    }

    @Transactional
    public WordSet addWordToWordSet(long wordSetId, WordMeta wordMeta) {
        StudiedWord unsavedStudiedWord = new StudiedWord(wordMeta.getText(), wordMeta.getTranslation());
        WordSet wordSet = getWordSet(wordSetId);

        wordSet.getStudiedWords().add(unsavedStudiedWord);
        return wordSet;
    }

    public WordSet getWordSet(long wordSetId) {
        return wordSetRepository.findByUserId(wordSetId)
                                .orElseThrow(() -> new DomainObjectNotFound(
                                        "Can't find word set with id " + wordSetId));
    }
}
