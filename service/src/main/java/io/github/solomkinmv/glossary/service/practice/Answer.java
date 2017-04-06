package io.github.solomkinmv.glossary.service.practice;

import io.github.solomkinmv.glossary.persistence.model.WordStage;
import lombok.Value;

@Value
public class Answer {
    long wordId;
    String answerText;
    WordStage stage;
    String image;
    String pronunciation;
}
