package io.github.solomkinmv.glossary.service.practice.quiz;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
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

    public Quiz generateQuiz(WordSet wordSet) {
        List<StudiedWord> words = wordSet.getStudiedWords();

        return new Quiz(
                words.stream()
                     .filter(notLearned())
                     .sorted(comparatorByLearningLevel())
                     .limit(TEST_SIZE)
                     .map(word -> createQuestion(word, words))
                     .collect(Collectors.toSet()));
    }

    private Quiz.Question createQuestion(StudiedWord studiedWord, List<StudiedWord> words) {
        Set<String> alternatives = generateAlternatives(studiedWord, words);

        return new Quiz.Question(
                studiedWord.getText(),
                createAnswer(studiedWord, false),
                alternatives);
    }
}
