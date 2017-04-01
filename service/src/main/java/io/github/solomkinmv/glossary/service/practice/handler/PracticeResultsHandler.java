package io.github.solomkinmv.glossary.service.practice.handler;

import io.github.solomkinmv.glossary.persistence.dao.StudiedWordDao;
import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import io.github.solomkinmv.glossary.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.service.practice.StatusUpdater;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PracticeResultsHandler {
    private final WordService wordService;
    private final StatusUpdater statusUpdater;
    private final StudiedWordDao studiedWordDao;

    public PracticeResultsHandler(WordService wordService, StatusUpdater statusUpdater, StudiedWordDao studiedWordDao) {
        this.wordService = wordService;
        this.statusUpdater = statusUpdater;
        this.studiedWordDao = studiedWordDao;
    }

    public void handle(PracticeResults results) {
        log.info("Handling quiz results: {}", results);
        results.getWordAnswers().forEach(this::handleAnswer);
    }

    private void handleAnswer(Long wordId, boolean correctAnswer) {
        StudiedWord studiedWord = wordService.getById(wordId)
                                             .orElseThrow(() -> new DomainObjectNotFound(
                                                            "No such studied word with id: " + wordId));

        handleAnswer(studiedWord, correctAnswer);
    }

    private void handleAnswer(StudiedWord word, boolean correctAnswer) {
        word.setStage(statusUpdater.compute(word.getStage(), correctAnswer));
        studiedWordDao.update(word);
    }
}