package io.github.solomkinmv.glossary.service.practice.writing;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.practice.provider.AbstractTestProvider;
import org.springframework.stereotype.Component;

import java.util.Random;
import java.util.stream.Collectors;

@Component
public class WritingTestProvider extends AbstractTestProvider {
    public WritingTestProvider() {
        super(new Random());
    }

    public WritingTestProvider(Random random) {
        super(random);
    }

    public WritingPracticeTest generateWritingTest(WordSet wordSet) {
        return new WritingPracticeTest(
                wordSet.getStudiedWords()
                       .stream()
                       .filter(notLearned())
                       .sorted(comparatorByLearningLevel())
                       .limit(TEST_SIZE)
                       .map(this::createQuestion)
                       .collect(Collectors.toSet()));
    }

    private WritingPracticeTest.Question createQuestion(StudiedWord studiedWord) {
        return new WritingPracticeTest.Question(
                studiedWord.getWord().getTranslation(),
                createAnswer(studiedWord, true));
    }
}
