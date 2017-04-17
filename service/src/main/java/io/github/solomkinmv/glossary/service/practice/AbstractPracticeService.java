package io.github.solomkinmv.glossary.service.practice;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import io.github.solomkinmv.glossary.service.practice.handler.PracticeResultsHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class AbstractPracticeService<T> implements PracticeService<T, PracticeResults> {
    private final WordSetService wordSetService;
    private final WordService wordService;
    private final PracticeResultsHandler practiceResultsHandler;

    protected AbstractPracticeService(WordSetService wordSetService,
                                      WordService wordService,
                                      PracticeResultsHandler practiceResultsHandler) {
        this.wordSetService = wordSetService;
        this.wordService = wordService;
        this.practiceResultsHandler = practiceResultsHandler;
    }

    @Override
    public T generateTest(String username, PracticeParameters practiceParameters) {
        log.info("Generating practice with parameters: {}", practiceParameters);

        Long wordSetId = practiceParameters.getWordSetId();
        List<StudiedWord> words;
        if (wordSetId != null) {
            words = wordSetService.getByIdAndUsername(wordSetId, username)
                                  .map(WordSet::getStudiedWords)
                                  .orElseThrow(() -> new DomainObjectNotFound(
                                          "No such word set with id: " + wordSetId));
        } else {
            words = wordService.listByUsername(username);
        }

        return generateTest(words, practiceParameters.isOriginQuestions());

    }

    @Override
    public void handle(PracticeResults result) {
        practiceResultsHandler.handle(result);
    }

    protected abstract T generateTest(List<StudiedWord> words, boolean originQuestions);
}
