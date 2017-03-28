package io.github.solomkinmv.glossary.service.practice.quiz;

import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.practice.AbstractPracticeService;
import io.github.solomkinmv.glossary.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.service.practice.handler.PracticeResultsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class QuizPracticeServiceImpl extends AbstractPracticeService<Quiz, PracticeResults> implements QuizPracticeService {
    private final QuizProvider quizProvider;
    private final PracticeResultsHandler practiceResultsHandler;

    @Autowired
    public QuizPracticeServiceImpl(WordSetService wordSetService, QuizProvider quizProvider, PracticeResultsHandler practiceResultsHandler) {
        super(wordSetService);
        this.practiceResultsHandler = practiceResultsHandler;
        this.quizProvider = quizProvider;
    }

    @Override
    protected Quiz generateTest(WordSet wordSet) {
        return quizProvider.generateQuiz(wordSet);
    }

    @Override
    public void handle(PracticeResults result) {
        practiceResultsHandler.handle(result);
    }
}
