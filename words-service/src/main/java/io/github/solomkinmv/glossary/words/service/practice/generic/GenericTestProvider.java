package io.github.solomkinmv.glossary.words.service.practice.generic;

import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import io.github.solomkinmv.glossary.words.service.practice.provider.AbstractTestProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static io.github.solomkinmv.glossary.words.service.practice.generic.GenericTest.GenericTestWord;

@Component
public class GenericTestProvider extends AbstractTestProvider {

    public GenericTestProvider() {
        super(new Random());
    }

    GenericTest generateWritingTest(List<Word> words, boolean originQuestions) {
        return new GenericTest(words.stream()
                                    .filter(word -> !word.getStage().equals(WordStage.LEARNED))
                                    .limit(TEST_SIZE)
                                    .map(word -> GenericTestWord.builder()
                                                                .wordId(word.getId())
                                                                .text(originQuestions ? word.getText() : word.getTranslation())
                                                                .translation(originQuestions ? word.getTranslation() : word.getText())
                                                                .image(word.getImage())
                                                                .sound(word.getSound())
                                                                .build())
                                    .collect(Collectors.toList()));
    }
}
