package io.github.solomkinmv.glossary.words.service.practice.repetition;


import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.words.service.practice.PracticeService;

import java.util.List;

public interface RepetitionPracticeService extends PracticeService<List<Word>, PracticeResults> {
}
