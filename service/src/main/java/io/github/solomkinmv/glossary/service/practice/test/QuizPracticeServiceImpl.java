package io.github.solomkinmv.glossary.service.practice.test;

import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.persistence.model.WordStage;
import io.github.solomkinmv.glossary.service.converter.WordConverter;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.dto.WordDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class QuizPracticeServiceImpl implements QuizPracticeService {
    public static final int TEST_SIZE = 10;
    public static final int NUMBER_OF_CHOICES = 5;
    private final WordConverter wordConverter;
    private final WordSetService wordSetService;
    private final Random random;

    @Autowired
    public QuizPracticeServiceImpl(WordConverter wordConverter, WordSetService wordSetService) {
        this(new Random(), wordConverter, wordSetService);
    }

    public QuizPracticeServiceImpl(Random random, WordConverter wordConverter, WordSetService wordSetService) {
        this.random = random;
        this.wordConverter = wordConverter;
        this.wordSetService = wordSetService;
    }

    @Override
    public QuizPractice generateQuiz(long wordSetId) {
        List<WordDto> words = wordSetService
                .getById(wordSetId)
                .map(WordSet::getStudiedWords)
                .orElseThrow(() -> new IllegalArgumentException("No such word set with id: " + wordSetId))
                .stream()
                .map(wordConverter::toDto)
                .collect(Collectors.toList());


        return new QuizPractice(
                words.stream()
                     .filter(notLearned())
                     .sorted(comparatorByLearningLevel())
                     .limit(TEST_SIZE)
                     .collect(Collectors.toMap(Function.identity(), word -> generateChoices(word, words),
                                               this::throwExceptionIfDuplicates)));
    }

    private Comparator<WordDto> comparatorByLearningLevel() {
        return Comparator.comparingInt(w -> w.getStage().ordinal());
    }

    private Predicate<WordDto> notLearned() {
        return studiedWord -> !studiedWord.getStage().equals(WordStage.LEARNED);
    }

    private QuizPractice.Choices throwExceptionIfDuplicates(QuizPractice.Choices choices1, QuizPractice.Choices choices2) {
        throw new IllegalStateException("Duplicated words during QuizPractice creation: " + choices1 + ", " + choices2);
    }

    private QuizPractice.Choices generateChoices(WordDto word, List<WordDto> wordList) {
        return new QuizPractice.Choices(
                Stream.generate(() -> getOtherRandomWord(wordList, word))
                      .limit(NUMBER_OF_CHOICES)
                      .collect(Collectors.toList()));
    }

    private WordDto getOtherRandomWord(List<WordDto> words, WordDto originalWord) {
        WordDto randomWord = getRandomWord(words);
        return randomWord.equals(originalWord) ? getOtherRandomWord(words, originalWord) : randomWord;
    }

    private WordDto getRandomWord(List<WordDto> words) {
        return words.get(random.nextInt(words.size()));
    }
}
