package io.github.solomkinmv.glossary.words.service.practice.generic;

import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import io.github.solomkinmv.glossary.words.service.practice.PracticeType;
import io.github.solomkinmv.glossary.words.service.practice.provider.AbstractTestProvider;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

import static io.github.solomkinmv.glossary.words.service.practice.generic.GenericTest.GenericTestWord;

@Component
public class GenericTestProvider extends AbstractTestProvider {

    private final Map<PracticeType, BiFunction<List<Word>, Boolean, GenericTest>> providerStrategies;

    public GenericTestProvider() {
        super(new Random());

        providerStrategies = Map.of(
                PracticeType.LEARNED_FIRST, this::generateGenericTestWithLearnedWords,
                PracticeType.LEARNING, this::generateGenericTestOnlyWithLearningWords,
                PracticeType.ALL, this::generateGenericTestWithAllWords
        );
    }

    private GenericTest generateGenericTestWithAllWords(List<Word> words, boolean originQuestions) {
        return new GenericTest(words.stream()
                                    .map(word -> createGenericTestWord(originQuestions, word))
                                    .collect(Collectors.toList()));
    }

    GenericTest generateGenericTest(List<Word> words, boolean originQuestions, PracticeType practiceType) {
        return providerStrategies.get(practiceType).apply(words, originQuestions);
    }

    private GenericTest generateGenericTestOnlyWithLearningWords(List<Word> words, boolean originQuestions) {
        return new GenericTest(words.stream()
                                    .filter(word -> !word.getStage().equals(WordStage.LEARNED))
                                    .limit(TEST_SIZE)
                                    .map(word -> createGenericTestWord(originQuestions, word))
                                    .collect(Collectors.toList()));
    }

    private GenericTest generateGenericTestWithLearnedWords(List<Word> words, boolean originQuestions) {
        return new GenericTest(words.stream()
                                    .sorted(Comparator.comparingInt(word -> word.getStage() == WordStage.LEARNED ? 0 : 1))
                                    .limit(TEST_SIZE)
                                    .map(word -> createGenericTestWord(originQuestions, word))
                                    .collect(Collectors.toList()));
    }

    private GenericTestWord createGenericTestWord(boolean originQuestions, Word word) {
        return GenericTestWord.builder()
                              .wordId(word.getId())
                              .text(originQuestions ? word.getText() : word.getTranslation())
                              .translation(originQuestions ? word.getTranslation() : word.getText())
                              .stage(word.getStage())
                              .image(word.getImage())
                              .sound(word.getSound())
                              .build();
    }
}
