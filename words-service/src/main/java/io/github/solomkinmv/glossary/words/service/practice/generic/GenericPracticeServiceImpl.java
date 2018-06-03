package io.github.solomkinmv.glossary.words.service.practice.generic;

import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import io.github.solomkinmv.glossary.words.service.practice.AbstractPracticeService;
import io.github.solomkinmv.glossary.words.service.practice.handler.PracticeResultsHandler;
import io.github.solomkinmv.glossary.words.service.word.WordService;
import io.github.solomkinmv.glossary.words.service.wordset.WordSetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GenericPracticeServiceImpl extends AbstractPracticeService<GenericTest> implements GenericPracticeService {

    @Autowired
    protected GenericPracticeServiceImpl(WordSetService wordSetService, WordService wordService, PracticeResultsHandler practiceResultsHandler) {
        super(wordSetService, wordService);
    }

    @Override
    protected GenericTest generateTest(List<Word> words, boolean originQuestions) {
        List<Word> learnedWords = words.stream()
                                       .filter(word -> !word.getStage().equals(WordStage.LEARNED))
                                       .collect(Collectors.toList());
        return new GenericTest(learnedWords);
    }
}
