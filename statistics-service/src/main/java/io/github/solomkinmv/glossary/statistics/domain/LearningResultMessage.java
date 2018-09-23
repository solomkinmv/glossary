package io.github.solomkinmv.glossary.statistics.domain;

import lombok.Data;

@Data
public class LearningResultMessage {
    private String subjectId;
    private String word;
    private WordStage stage;
    private LearningResult result;
}
