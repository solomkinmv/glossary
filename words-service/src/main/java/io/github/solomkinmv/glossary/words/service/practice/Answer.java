package io.github.solomkinmv.glossary.words.service.practice;

import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import lombok.Value;

@Value
public class Answer {

    long wordId;
    String answerText;
    WordStage stage;
    String image;
    String pronunciation;
}
