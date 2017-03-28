package io.github.solomkinmv.glossary.service.practice.quiz.handler;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.service.domain.StudiedWordService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import io.github.solomkinmv.glossary.service.practice.StatusUpdater;
import io.github.solomkinmv.glossary.service.practice.quiz.QuizResults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class QuizResultHandler {
    private final StudiedWordService studiedWordService;
    private final StatusUpdater statusUpdater;

    public QuizResultHandler(StudiedWordService studiedWordService, StatusUpdater statusUpdater) {
        this.studiedWordService = studiedWordService;
        this.statusUpdater = statusUpdater;
    }

    public void handle(QuizResults results) {
        log.info("Handling quiz results: {}", results);
        results.getWordAnswers().forEach(this::handleAnswer);
    }

    private void handleAnswer(Long wordId, boolean correctAnswer) {
        StudiedWord studiedWord = studiedWordService.getById(wordId)
                                                    .orElseThrow(() -> new DomainObjectNotFound(
                                                            "No such studied word with id: " + wordId));

        handleAnswer(studiedWord, correctAnswer);
    }

    private void handleAnswer(StudiedWord word, boolean correctAnswer) {
        word.setStage(statusUpdater.compute(word.getStage(), correctAnswer));
        studiedWordService.update(word);
    }
}