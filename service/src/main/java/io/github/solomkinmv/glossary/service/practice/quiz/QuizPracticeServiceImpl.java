package io.github.solomkinmv.glossary.service.practice.quiz;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.practice.AbstractPracticeService;
import io.github.solomkinmv.glossary.service.practice.handler.PracticeResultsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class QuizPracticeServiceImpl extends AbstractPracticeService<Quiz> implements QuizPracticeService {
    private final QuizProvider quizProvider;

    @Autowired
    public QuizPracticeServiceImpl(WordSetService wordSetService,
                                   WordService wordService,
                                   PracticeResultsHandler practiceResultsHandler, QuizProvider quizProvider) {
        super(wordSetService, wordService, practiceResultsHandler);
        this.quizProvider = quizProvider;
    }

    @Override
    protected Quiz generateTest(List<StudiedWord> words, boolean originQuestions) {
        return quizProvider.generateQuiz(words, originQuestions);
    }
}
