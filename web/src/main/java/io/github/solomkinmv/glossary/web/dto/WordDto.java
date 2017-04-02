package io.github.solomkinmv.glossary.web.dto;

import io.github.solomkinmv.glossary.persistence.model.WordStage;
import lombok.Value;

@Value
public class WordDto {
    Long id;
    String text;
    String translation;
    WordStage stage;
    String image;
    String sound;
}
