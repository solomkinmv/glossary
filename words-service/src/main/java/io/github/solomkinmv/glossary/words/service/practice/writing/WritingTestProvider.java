package io.github.solomkinmv.glossary.words.service.practice.writing;

import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.service.practice.provider.AbstractTestProvider;
import org.springframework.stereotype.Component;

import java.util.List;
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

    WritingPracticeTest generateWritingTest(List<Word> words, boolean originQuestions) {
        return new WritingPracticeTest(
                words
                        .stream()
                        .filter(notLearned())
                        .sorted(comparatorByLearningLevel())
                        .limit(TEST_SIZE)
                        .map(studiedWord -> createQuestion(studiedWord, originQuestions))
                        .collect(Collectors.toSet()));
    }

    private WritingPracticeTest.Question createQuestion(Word studiedWord, boolean originQuestions) {
        return new WritingPracticeTest.Question(
                originQuestions ? studiedWord.getText() : studiedWord.getTranslation(),
                createAnswer(studiedWord, !originQuestions));
    }
}
