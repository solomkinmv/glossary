package io.github.solomkinmv.glossary.service.practice;

import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractPracticeService<T, R> implements PracticeService<T, R> {
    private final WordSetService wordSetService;

    protected AbstractPracticeService(WordSetService wordSetService) {
        this.wordSetService = wordSetService;
    }

    @Override
    public T generateTest(long wordSetId) {
        log.info("Generating quiz for wordSet with id {}", wordSetId);
        WordSet wordSet = wordSetService.getById(wordSetId)
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                "No such word set with id: " + wordSetId));
        return generateTest(wordSet);
    }

    protected abstract T generateTest(WordSet wordSet);
}
