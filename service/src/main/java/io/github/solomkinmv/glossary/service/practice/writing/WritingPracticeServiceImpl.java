package io.github.solomkinmv.glossary.service.practice.writing;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.practice.AbstractPracticeService;
import io.github.solomkinmv.glossary.service.practice.handler.PracticeResultsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class WritingPracticeServiceImpl extends AbstractPracticeService<WritingPracticeTest> implements WritingPracticeService {
    private final WritingTestProvider writingTestProvider;

    @Autowired
    protected WritingPracticeServiceImpl(WordSetService wordSetService,
                                         WordService wordService,
                                         PracticeResultsHandler practiceResultsHandler,
                                         WritingTestProvider writingTestProvider) {
        super(wordSetService, wordService, practiceResultsHandler);
        this.writingTestProvider = writingTestProvider;
    }

    @Override
    protected WritingPracticeTest generateTest(List<StudiedWord> words, boolean originQuestions) {
        return writingTestProvider.generateWritingTest(words, originQuestions);
    }
}
