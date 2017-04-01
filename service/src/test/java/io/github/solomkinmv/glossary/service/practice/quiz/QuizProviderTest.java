package io.github.solomkinmv.glossary.service.practice.quiz;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.persistence.model.WordStage;
import io.github.solomkinmv.glossary.service.practice.Answer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static io.github.solomkinmv.glossary.service.practice.provider.AbstractTestProvider.NUMBER_OF_CHOICES;
import static io.github.solomkinmv.glossary.service.practice.provider.AbstractTestProvider.TEST_SIZE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class QuizProviderTest {
    private static final long SEED = 42;

    private QuizProvider quizProvider = new QuizProvider(new Random(SEED));

    @Test
    public void generatePracticeTest() {
        Quiz quiz = quizProvider.generateQuiz(createWordSet());

        Set<Quiz.Question> questions = quiz.getQuestions();
        assertEquals(TEST_SIZE, questions.size());

        long countOfNotStudiedWords = questions.stream()
                                               .map(Quiz.Question::getAnswer)
                                               .map(Answer::getStage)
                                               .filter(WordStage.NOT_LEARNED::equals)
                                               .count();
        assertEquals(2, countOfNotStudiedWords);

        assertTrue(questions.stream()
                            .map(Quiz.Question::getAlternatives)
                            .map(Set::size)
                            .allMatch(size -> size == NUMBER_OF_CHOICES));
    }

    private WordSet createWordSet() {
        return new WordSet("name", "description", createStudiedWords());
    }

    private List<StudiedWord> createStudiedWords() {
        return Arrays.asList(
                new StudiedWord(1L, "text1", "translation1", WordStage.NOT_LEARNED),
                new StudiedWord(2L, "text2", "translation2", WordStage.NOT_LEARNED),
                new StudiedWord(3L, "text3", "translation3", WordStage.LEARNING),
                new StudiedWord(4L, "text4", "translation4", WordStage.LEARNING),
                new StudiedWord(5L, "text5", "translation5", WordStage.LEARNING),
                new StudiedWord(6L, "text6", "translation6", WordStage.LEARNING),
                new StudiedWord(7L, "text7", "translation7", WordStage.LEARNING),
                new StudiedWord(8L, "text8", "translation8", WordStage.LEARNING),
                new StudiedWord(9L, "text9", "translation9", WordStage.LEARNING),
                new StudiedWord(10L, "text10", "translation10", WordStage.LEARNING),
                new StudiedWord(11L, "text11", "translation11", WordStage.LEARNING),
                new StudiedWord(12L, "text12", "translation12", WordStage.LEARNED),
                new StudiedWord(13L, "text13", "translation13", WordStage.LEARNED)
        );
    }
}