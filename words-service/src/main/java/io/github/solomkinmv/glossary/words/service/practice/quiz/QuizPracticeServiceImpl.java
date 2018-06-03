package io.github.solomkinmv.glossary.words.service.practice.quiz;

import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.service.practice.AbstractPracticeService;
import io.github.solomkinmv.glossary.words.service.practice.handler.PracticeResultsHandler;
import io.github.solomkinmv.glossary.words.service.word.WordService;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetService;
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
                                   PracticeResultsHandler practiceResultsHandler,
                                   QuizProvider quizProvider) {
        super(wordSetService, wordService);
        this.quizProvider = quizProvider;
    }

    @Override
    protected Quiz generateTest(List<Word> words, boolean originQuestions) {
        return quizProvider.generateQuiz(words, originQuestions);
    }
}
