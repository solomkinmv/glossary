package io.github.solomkinmv.glossary.service.practice.quiz;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.service.practice.provider.AbstractTestProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class QuizProvider extends AbstractTestProvider {

    public QuizProvider(Random random) {
        super(random);
    }

    public QuizProvider() {
        this(new Random());
    }

    Quiz generateQuiz(List<StudiedWord> words, boolean originQuestions) {
        return new Quiz(
                words.stream()
                     .filter(notLearned())
                     .sorted(comparatorByLearningLevel())
                     .limit(TEST_SIZE)
                     .map(word -> createQuestion(word, words, originQuestions))
                     .collect(Collectors.toSet()));
    }

    private Quiz.Question createQuestion(StudiedWord studiedWord, List<StudiedWord> words, boolean originQuestions) {
        Set<String> alternatives = generateAlternatives(studiedWord, words, !originQuestions);

        return new Quiz.Question(
                originQuestions ? studiedWord.getText() : studiedWord.getTranslation(),
                createAnswer(studiedWord, !originQuestions),
                alternatives);
    }
}
