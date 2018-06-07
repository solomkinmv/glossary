package io.github.solomkinmv.glossary.words.service.practice.generic;

import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.service.practice.AbstractPracticeService;
import io.github.solomkinmv.glossary.words.service.practice.handler.PracticeResultsHandler;
import io.github.solomkinmv.glossary.words.service.word.WordService;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenericPracticeServiceImpl extends AbstractPracticeService<GenericTest> implements GenericPracticeService {

    private final GenericTestProvider generateWritingTest;

    @Autowired
    protected GenericPracticeServiceImpl(WordSetService wordSetService, WordService wordService, PracticeResultsHandler practiceResultsHandler, GenericTestProvider generateWritingTest) {
        super(wordSetService, wordService);
        this.generateWritingTest = generateWritingTest;
    }

    @Override
    protected GenericTest generateTest(List<Word> words, boolean originQuestions) {
        return generateWritingTest.generateWritingTest(words);
    }
}
