package io.github.solomkinmv.glossary.words.service.practice.generic;

import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import io.github.solomkinmv.glossary.words.service.practice.provider.AbstractTestProvider;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class GenericTestProvider extends AbstractTestProvider {

    public GenericTestProvider() {
        super(new Random());
    }

    GenericTest generateWritingTest(List<Word> words) {
        List<Word> learnedWords = words.stream()
                                       .filter(word -> !word.getStage().equals(WordStage.LEARNED))
                                       .limit(TEST_SIZE)
                                       .collect(Collectors.toList());
        return new GenericTest(learnedWords);
    }
}
