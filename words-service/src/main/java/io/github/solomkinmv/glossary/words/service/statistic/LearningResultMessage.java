package io.github.solomkinmv.glossary.words.service.statistic;

import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LearningResultMessage {
    private String subjectId;
    private Long wordId;
    private WordStage stage;
    private LearningResult result;

    public enum LearningResult {
        CORRECT, INCORRECT
    }
}
