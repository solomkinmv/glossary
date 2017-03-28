package io.github.solomkinmv.glossary.service.practice.quiz.provider;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.Word;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.persistence.model.WordStage;
import io.github.solomkinmv.glossary.service.practice.quiz.Quiz;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.github.solomkinmv.glossary.service.practice.quiz.QuizPracticeServiceImpl.NUMBER_OF_CHOICES;
import static io.github.solomkinmv.glossary.service.practice.quiz.QuizPracticeServiceImpl.TEST_SIZE;

@Component
public class QuizProvider {
    private final Random random;

    public QuizProvider(Random random) {
        this.random = random;
    }

    public QuizProvider() {
        this(new Random());
    }

    public Quiz generateQuiz(WordSet wordSet) {
        List<StudiedWord> words = wordSet.getStudiedWords();

        return new Quiz(
                words.stream()
                     .sorted(comparatorByLearningLevel())
                     .limit(TEST_SIZE)
                     .map(word -> createQuestion(word, words))
                     .collect(Collectors.toSet()));
    }

    private Comparator<StudiedWord> comparatorByLearningLevel() {
        return Comparator.comparingInt(w -> w.getStage().ordinal());
    }

    private Quiz.Question createQuestion(StudiedWord studiedWord, List<StudiedWord> words) {
        Set<String> alternatives = generateAlternatives(studiedWord, words);

        return new Quiz.Question(
                studiedWord.getWord().getText(),
                createAnswer(studiedWord),
                alternatives);
    }

    private Quiz.Answer createAnswer(StudiedWord studiedWord) {
        return new Quiz.Answer(
                studiedWord.getId(),
                studiedWord.getWord().getTranslation(),
                studiedWord.getStage(),
                studiedWord.getWord().getImage(),
                studiedWord.getWord().getSound());
    }


    private Predicate<StudiedWord> notLearned() {
        return studiedWord -> !studiedWord.getStage().equals(WordStage.LEARNED);
    }

    private Set<String> generateAlternatives(StudiedWord word, List<StudiedWord> wordList) {
        if (wordList.size() < NUMBER_OF_CHOICES) {
            return generateAlternatives(wordList.stream());
        }
        return generateAlternatives(Stream.generate(() -> getOtherRandomWord(wordList, word)));
    }

    private Set<String> generateAlternatives(Stream<StudiedWord> wordStream) {
        return wordStream
                .map(StudiedWord::getWord)
                .map(Word::getTranslation)
                .distinct()
                .limit(NUMBER_OF_CHOICES)
                .collect(Collectors.toSet());
    }

    private StudiedWord getOtherRandomWord(List<StudiedWord> words, StudiedWord originalWord) {
        StudiedWord randomWord = getRandomWord(words);
        return randomWord.equals(originalWord) ? getOtherRandomWord(words, originalWord) : randomWord;
    }

    private StudiedWord getRandomWord(List<StudiedWord> words) {
        return words.get(random.nextInt(words.size()));
    }
}
