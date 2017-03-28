package io.github.solomkinmv.glossary.service.practice.quiz;

import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.StudiedWordService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.practice.StatusUpdater;
import io.github.solomkinmv.glossary.service.practice.quiz.handler.QuizResultHandler;
import io.github.solomkinmv.glossary.service.practice.quiz.provider.QuizProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QuizPracticeServiceImpl implements QuizPracticeService {
    public static final int TEST_SIZE = 10;
    public static final int NUMBER_OF_CHOICES = 5;
    private final WordSetService wordSetService;
    private final QuizProvider quizProvider;
    private final QuizResultHandler quizResultHandler;

    @Autowired
    public QuizPracticeServiceImpl(WordSetService wordSetService, StudiedWordService studiedWordService, StatusUpdater statusUpdater, QuizProvider quizProvider, QuizResultHandler quizResultHandler) {
        this.wordSetService = wordSetService;
        this.quizResultHandler = quizResultHandler;
        this.quizProvider = quizProvider;
    }


    @Override
    public Quiz generateQuiz(long wordSetId) {
        log.info("Generating quiz for wordSet with id {}", wordSetId);
        WordSet wordSet = wordSetService.getById(wordSetId)
                                        .orElseThrow(() -> new IllegalArgumentException(
                                                "No such word set with id: " + wordSetId));

        return quizProvider.generateQuiz(wordSet);
    }

    @Override
    public void handleResults(QuizResults results) {
        quizResultHandler.handle(results);
    }
}
