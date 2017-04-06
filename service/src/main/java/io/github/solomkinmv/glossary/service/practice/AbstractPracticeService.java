package io.github.solomkinmv.glossary.service.practice;

import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractPracticeService<T, R> implements PracticeService<T, R> {
    private final WordSetService wordSetService;

    protected AbstractPracticeService(WordSetService wordSetService) {
        this.wordSetService = wordSetService;
    }

    @Override
    public T generateTest(String username, long wordSetId) {
        log.info("Generating quiz for wordSet with id {}", wordSetId);
        WordSet wordSet = wordSetService.getByIdAndUsername(wordSetId, username)
                                        .orElseThrow(() -> new DomainObjectNotFound(
                                                "No such word set with id: " + wordSetId));
        return generateTest(wordSet);
    }

    protected abstract T generateTest(WordSet wordSet);
}
