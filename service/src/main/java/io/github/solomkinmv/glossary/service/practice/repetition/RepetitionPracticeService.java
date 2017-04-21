package io.github.solomkinmv.glossary.service.practice.repetition;

import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import io.github.solomkinmv.glossary.service.practice.PracticeResults;
import io.github.solomkinmv.glossary.service.practice.PracticeService;

import java.util.List;

public interface RepetitionPracticeService extends PracticeService<List<StudiedWord>, PracticeResults> {
}
