package io.github.solomkinmv.glossary.words.service.practice.handler;

import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import io.github.solomkinmv.glossary.words.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.words.service.practice.StatusUpdater;
import io.github.solomkinmv.glossary.words.service.word.WordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class PracticeResultsHandler {
    private final WordService wordService;
    private final StatusUpdater statusUpdater;

    public void handle(PracticeResults results) {
        log.info("Handling quiz results: {}", results);
        results.getWordAnswers().forEach(this::handleAnswer);
    }

    private void handleAnswer(Long wordId, boolean correctAnswer) {
        Word word = wordService.getById(wordId);

        WordStage updatedStage = statusUpdater.compute(word.getStage(), correctAnswer);
        word.setStage(updatedStage);
        wordService.save(word);
    }
}