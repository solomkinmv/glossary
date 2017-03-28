package io.github.solomkinmv.glossary.service.practice.writing;

import io.github.solomkinmv.glossary.persistence.model.WordSet;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.practice.AbstractPracticeService;
import io.github.solomkinmv.glossary.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.service.practice.handler.PracticeResultsHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class WritingPracticeServiceImpl extends AbstractPracticeService<WritingPracticeTest, PracticeResults> implements WritingPracticeService {
    private final PracticeResultsHandler practiceResultsHandler;
    private final WritingTestProvider writingTestProvider;

    @Autowired
    protected WritingPracticeServiceImpl(WordSetService wordSetService, PracticeResultsHandler practiceResultsHandler, WritingTestProvider writingTestProvider) {
        super(wordSetService);
        this.practiceResultsHandler = practiceResultsHandler;
        this.writingTestProvider = writingTestProvider;
    }

    @Override
    protected WritingPracticeTest generateTest(WordSet wordSet) {
        return writingTestProvider.generateWritingTest(wordSet);
    }

    @Override
    public void handle(PracticeResults result) {
        practiceResultsHandler.handle(result);
    }
}
