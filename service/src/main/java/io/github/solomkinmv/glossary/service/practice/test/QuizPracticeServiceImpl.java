package io.github.solomkinmv.glossary.service.practice.test;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.persistence.model.WordStage;
import io.github.solomkinmv.glossary.service.converter.WordConverter;
import io.github.solomkinmv.glossary.service.domain.StudiedWordService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.dto.WordDto;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
import io.github.solomkinmv.glossary.service.practice.StatusUpdater;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class QuizPracticeServiceImpl implements QuizPracticeService {
    public static final int TEST_SIZE = 10;
    public static final int NUMBER_OF_CHOICES = 5;
    private final WordConverter wordConverter;
    private final WordSetService wordSetService;
    private final StudiedWordService studiedWordService;
    private final StatusUpdater statusUpdater;
    private final Random random;

    @Autowired
    public QuizPracticeServiceImpl(WordConverter wordConverter, WordSetService wordSetService, StudiedWordService studiedWordService, StatusUpdater statusUpdater) {
        this(new Random(), wordConverter, wordSetService, studiedWordService, statusUpdater);
    }

    public QuizPracticeServiceImpl(Random random, WordConverter wordConverter, WordSetService wordSetService, StudiedWordService studiedWordService, StatusUpdater statusUpdater) {
        this.random = random;
        this.wordConverter = wordConverter;
        this.wordSetService = wordSetService;
        this.studiedWordService = studiedWordService;
        this.statusUpdater = statusUpdater;
    }

    @Override
    public QuizPractice generateQuiz(long wordSetId) {
        log.info("Generating quiz for wordSet with id {}", wordSetId);
        List<WordDto> words = getWords(wordSetId);

        return new QuizPractice(
                words.stream()
                     .filter(notLearned())
                     .sorted(comparatorByLearningLevel())
                     .limit(TEST_SIZE)
                     .collect(Collectors.toMap(Function.identity(), word -> generateChoices(word, words),
                                               this::throwExceptionIfDuplicates)));
    }

    @Override
    public void handleResults(QuizResults results) {
        log.info("Handling quiz results: {}", results);
        results.getWordAnswers().forEach(this::handleAnswer);
    }

    private List<WordDto> getWords(long wordSetId) {
        return wordSetService
                .getById(wordSetId)
                .map(WordSet::getStudiedWords)
                .orElseThrow(() -> new IllegalArgumentException("No such word set with id: " + wordSetId))
                .stream()
                .map(wordConverter::toDto)
                .collect(Collectors.toList());
    }

    private void handleAnswer(Long wordId, boolean correctAnswer) {
        StudiedWord studiedWord = studiedWordService.getById(wordId)
                                                    .orElseThrow(() -> new DomainObjectNotFound(
                                                            "No such studied word with id: " + wordId));

        handleAnswer(studiedWord, correctAnswer);
    }

    private void handleAnswer(StudiedWord word, boolean correctAnswer) {
        word.setStage(statusUpdater.compute(word.getStage(), correctAnswer));
        studiedWordService.update(word);
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
