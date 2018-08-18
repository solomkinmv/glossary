package io.github.solomkinmv.glossary.words.service.practice.generic;

import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.service.practice.AbstractPracticeService;
import io.github.solomkinmv.glossary.words.service.word.WordService;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenericPracticeServiceImpl extends AbstractPracticeService<GenericTest> implements GenericPracticeService {

    private final GenericTestProvider genericTestProvider;

    @Autowired
    protected GenericPracticeServiceImpl(WordSetService wordSetService, WordService wordService,
                                         GenericTestProvider genericTestProvider) {
        super(wordSetService, wordService);
        this.genericTestProvider = genericTestProvider;
    }

    @Override
    protected GenericTest generateTest(List<Word> words, boolean originQuestions) {
        return genericTestProvider.generateWritingTest(words, originQuestions);
    }
}
