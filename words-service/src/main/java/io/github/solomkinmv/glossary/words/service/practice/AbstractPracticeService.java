package io.github.solomkinmv.glossary.words.service.practice;

import io.github.solomkinmv.glossary.words.exception.DomainObjectNotFound;
import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import io.github.solomkinmv.glossary.words.service.practice.handler.PracticeResultsHandler;
import io.github.solomkinmv.glossary.words.service.word.WordService;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public abstract class AbstractPracticeService<T> implements PracticeService<T> {
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
    public T generateTest(long userId, PracticeParameters practiceParameters) {
        log.info("Generating practice with parameters: {}", practiceParameters);

        Long wordSetId = practiceParameters.getWordSetId();
        List<Word> words;
        if (wordSetId != null) {
            words = wordSetService.findWordSet(wordSetId)
                                  .map(WordSet::getWords)
                                  .orElseThrow(() -> new DomainObjectNotFound(
                                          "No such word set with id: " + wordSetId));
        } else {
            words = wordService.findAllByUserId(userId);
        }

        return generateTest(words, practiceParameters.isOriginQuestions());

    }

    @Override
    public void handle(PracticeResults result) {
        practiceResultsHandler.handle(result);
    }

    protected abstract T generateTest(List<Word> words, boolean originQuestions);
}
