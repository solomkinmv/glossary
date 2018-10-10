package io.github.solomkinmv.glossary.statistics.listener;

import io.github.solomkinmv.glossary.statistics.domain.WordStage;
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
