package io.github.solomkinmv.glossary.words.service.practice;

import io.github.solomkinmv.glossary.words.exception.DomainObjectNotFound;
import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import io.github.solomkinmv.glossary.words.service.word.WordService;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@AllArgsConstructor
public abstract class AbstractPracticeService<T> implements PracticeService<T> {
    private final WordSetService wordSetService;
    private final WordService wordService;


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

    protected abstract T generateTest(List<Word> words, boolean originQuestions);
}
