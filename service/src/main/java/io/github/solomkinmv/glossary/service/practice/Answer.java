package io.github.solomkinmv.glossary.service.practice;

import io.github.solomkinmv.glossary.persistence.model.WordStage;
import lombok.Value;

import java.net.URL;

@Value
public class Answer {
    long wordId;
    String answer;
    WordStage stage;
    URL image;
    URL pronunciation;
}
