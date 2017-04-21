package io.github.solomkinmv.glossary.service.practice.repetition;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.persistence.model.WordStage;
import io.github.solomkinmv.glossary.service.domain.WordService;
import io.github.solomkinmv.glossary.service.domain.WordSetService;
import io.github.solomkinmv.glossary.service.practice.AbstractPracticeService;
import io.github.solomkinmv.glossary.service.practice.handler.PracticeResultsHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepetitionPracticeServiceImpl extends AbstractPracticeService<List<StudiedWord>> implements RepetitionPracticeService {

    @Autowired
    protected RepetitionPracticeServiceImpl(WordSetService wordSetService, WordService wordService, PracticeResultsHandler practiceResultsHandler) {
        super(wordSetService, wordService, practiceResultsHandler);
    }

    @Override
    protected List<StudiedWord> generateTest(List<StudiedWord> words, boolean originQuestions) {
        return words.stream()
                    .filter(word -> WordStage.LEARNED.equals(word.getStage()))
                    .collect(Collectors.toList());
    }
}
