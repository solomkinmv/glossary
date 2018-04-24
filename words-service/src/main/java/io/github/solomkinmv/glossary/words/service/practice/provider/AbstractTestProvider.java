package io.github.solomkinmv.glossary.words.service.practice.provider;


import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import io.github.solomkinmv.glossary.words.service.practice.Answer;

import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractTestProvider {
    public static final int NUMBER_OF_CHOICES = 5;
    public static final int TEST_SIZE = 10;
    protected final Random random;

    public AbstractTestProvider(Random random) {
        this.random = random;
    }

    protected Predicate<Word> notLearned() {
        return studiedWord -> !studiedWord.getStage().equals(WordStage.LEARNED);
    }

    protected Set<String> generateAlternatives(Word word, List<Word> wordList, boolean origin) {
        if (wordList.size() < NUMBER_OF_CHOICES) {
            return generateAlternatives(wordList.stream(), origin);
        }
        Stream<Word> originalWord = Stream.of(word);
        Stream<Word> randomWords = Stream.generate(() -> getOtherRandomWord(wordList, word));
        return generateAlternatives(Stream.concat(originalWord, randomWords), origin);
    }

    private Set<String> generateAlternatives(Stream<Word> wordStream, boolean origin) {
        Function<Word, String> toAlternative = (word) -> origin ? word.getText() : word.getTranslation();
        return wordStream
                .map(toAlternative)
                .distinct()
                .limit(NUMBER_OF_CHOICES)
                .collect(Collectors.toSet());
    }

    private Word getOtherRandomWord(List<Word> words, Word originalWord) {
        Word randomWord = getRandomWord(words);
        return randomWord.equals(originalWord) ? getOtherRandomWord(words, originalWord) : randomWord;
    }

    private Word getRandomWord(List<Word> words) {
        return words.get(random.nextInt(words.size()));
    }

    protected Comparator<Word> comparatorByLearningLevel() {
        return Comparator.comparingInt(w -> w.getStage().ordinal());
    }

    protected Answer createAnswer(Word word, boolean origin) {
        return new Answer(
                word.getId(),
                origin ? word.getText() : word.getTranslation(),
                word.getStage(),
                word.getImage(),
                word.getSound());
    }
}
