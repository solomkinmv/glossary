package io.github.solomkinmv.glossary.words.service;

import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import io.github.solomkinmv.glossary.words.persistence.repository.WordSetRepository;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class WordSetService {

    private final WordSetRepository wordSetRepository;

    public List<WordSet> findAllForUserId(long userId) {
        return wordSetRepository.findAllByUserId(userId);
    }

    public long create(WordSetMeta wordSetMeta) {
        WordSet wordSet = new WordSet(wordSetMeta.getName(), wordSetMeta.getDescription());
        return wordSetRepository.save(wordSet).getId();
    }
}
