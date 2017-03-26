package io.github.solomkinmv.glossary.service.practice;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.persistence.model.WordStage;
import io.github.solomkinmv.glossary.service.converter.WordConverter;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.dto.WordDto;
import io.github.solomkinmv.glossary.service.practice.test.QuizPractice;
import io.github.solomkinmv.glossary.service.practice.test.QuizPracticeServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.*;

import static io.github.solomkinmv.glossary.service.practice.test.QuizPracticeServiceImpl.NUMBER_OF_CHOICES;
import static io.github.solomkinmv.glossary.service.practice.test.QuizPracticeServiceImpl.TEST_SIZE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QuizPracticeServiceImplQuiz {
    private static final long SEED = 42;

    @Mock
    private WordSetService wordSetService;
    private QuizPracticeServiceImpl practiceService;


    @Before
    public void setUp() throws Exception {
        when(wordSetService.getById(1L)).thenReturn(Optional.of(createWordSet()));
        when(wordSetService.getById(2L)).thenReturn(Optional.empty());

        practiceService = new QuizPracticeServiceImpl(new Random(SEED), new WordConverter(), wordSetService);
    }

    @Test
    public void generatePracticeTest() throws Exception {
        int wordSetId = 1;

        QuizPractice quizPractice = practiceService.generateQuiz(wordSetId);

        Map<WordDto, QuizPractice.Choices> words = quizPractice.getWords();
        assertEquals(TEST_SIZE, words.size());

        long countOfNotStudiedWords = words.keySet().stream()
                                           .map(WordDto::getStage)
                                           .filter(WordStage.NOT_STUDIED::equals)
                                           .count();
        assertEquals(2, countOfNotStudiedWords);

        assertTrue(words.values().stream()
                        .map(QuizPractice.Choices::getAlternatives)
                        .map(List::size)
                        .allMatch(size -> size == NUMBER_OF_CHOICES));

        assertTrue(words.entrySet().stream()
                        .map(e -> checkNonRepeatableChoices(e.getKey(), e.getValue()))
                        .allMatch(Boolean.TRUE::equals));
    }

    private boolean checkNonRepeatableChoices(WordDto wordDto, QuizPractice.Choices choices) {
        return !choices.getAlternatives().contains(wordDto);
    }

    private WordSet createWordSet() {
        return new WordSet("name", "description", createStudiedWords());
    }

    private List<StudiedWord> createStudiedWords() {
        return Arrays.asList(
                new StudiedWord(new Word("text1", "translation"), WordStage.NOT_STUDIED),
                new StudiedWord(new Word("text2", "translation"), WordStage.NOT_STUDIED),
                new StudiedWord(new Word("text3", "translation"), WordStage.IN_PROGRESS),
                new StudiedWord(new Word("text4", "translation"), WordStage.IN_PROGRESS),
                new StudiedWord(new Word("text5", "translation"), WordStage.IN_PROGRESS),
                new StudiedWord(new Word("text6", "translation"), WordStage.IN_PROGRESS),
                new StudiedWord(new Word("text7", "translation"), WordStage.IN_PROGRESS),
                new StudiedWord(new Word("text8", "translation"), WordStage.IN_PROGRESS),
                new StudiedWord(new Word("text9", "translation"), WordStage.IN_PROGRESS),
                new StudiedWord(new Word("text10", "translation"), WordStage.IN_PROGRESS),
                new StudiedWord(new Word("text11", "translation"), WordStage.IN_PROGRESS),
                new StudiedWord(new Word("text12", "translation"), WordStage.LEARNED),
                new StudiedWord(new Word("text13", "translation"), WordStage.LEARNED)
        );
    }
}