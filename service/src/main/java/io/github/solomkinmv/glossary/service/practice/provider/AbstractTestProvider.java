package io.github.solomkinmv.glossary.service.practice.provider;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.WordStage;
import io.github.solomkinmv.glossary.service.practice.Answer;

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

    protected Predicate<StudiedWord> notLearned() {
        return studiedWord -> !studiedWord.getStage().equals(WordStage.LEARNED);
    }

    protected Set<String> generateAlternatives(StudiedWord word, List<StudiedWord> wordList, boolean origin) {
        if (wordList.size() < NUMBER_OF_CHOICES) {
            return generateAlternatives(wordList.stream(), origin);
        }
        Stream<StudiedWord> originalWord = Stream.of(word);
        Stream<StudiedWord> randomWords = Stream.generate(() -> getOtherRandomWord(wordList, word));
        return generateAlternatives(Stream.concat(originalWord, randomWords), origin);
    }

    private Set<String> generateAlternatives(Stream<StudiedWord> wordStream, boolean origin) {
        Function<StudiedWord, String> toAlternative = (word) -> origin ? word.getText() : word.getTranslation();
        return wordStream
                .map(toAlternative)
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

    protected Comparator<StudiedWord> comparatorByLearningLevel() {
        return Comparator.comparingInt(w -> w.getStage().ordinal());
    }

    protected Answer createAnswer(StudiedWord word, boolean origin) {
        return new Answer(
                word.getId(),
                origin ? word.getText() : word.getTranslation(),
                word.getStage(),
                word.getImage(),
                word.getSound());
    }
}
