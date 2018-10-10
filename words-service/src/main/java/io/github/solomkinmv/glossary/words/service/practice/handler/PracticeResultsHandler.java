package io.github.solomkinmv.glossary.words.service.practice.handler;

import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import io.github.solomkinmv.glossary.words.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.words.service.practice.status.StatusUpdater;
import io.github.solomkinmv.glossary.words.service.statistic.LearningResultMessage;
import io.github.solomkinmv.glossary.words.service.statistic.StatisticService;
import io.github.solomkinmv.glossary.words.service.word.WordService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static io.github.solomkinmv.glossary.words.service.statistic.LearningResultMessage.LearningResult.CORRECT;
import static io.github.solomkinmv.glossary.words.service.statistic.LearningResultMessage.LearningResult.INCORRECT;

@Component
@Slf4j
@AllArgsConstructor
public class PracticeResultsHandler {

    private final WordService wordService;
    private final StatusUpdater statusUpdater;
    private final StatisticService statisticService;

    public void handle(PracticeResults results, String subjectId) {
        log.info("Handling quiz results: {}", results);
        results.getWordAnswers()
               .forEach((wordId, correctAnswer) -> handleAnswer(wordId, correctAnswer, subjectId));
    }

    private void handleAnswer(Long wordId, boolean correctAnswer, String subjectId) {
        Word word = wordService.getById(wordId);

        WordStage updatedStage = statusUpdater.compute(word.getStage(), correctAnswer);
        word.setStage(updatedStage);
        wordService.save(word);

        statisticService.reportStats(LearningResultMessage.builder()
                                                          .subjectId(subjectId)
                                                          .wordId(wordId)
                                                          .result(correctAnswer ? CORRECT : INCORRECT)
                                                          .stage(updatedStage)
                                                          .build());
    }
}