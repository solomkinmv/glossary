package io.github.solomkinmv.glossary.web.dto;

import io.github.solomkinmv.glossary.persistence.model.WordStage;
import lombok.Value;

import javax.validation.constraints.Null;

@Value
public class WordDto {
    @Null
    Long id;
    String text;
    String translation;
    @Null
    WordStage stage;
    String image;
    @Null
    String sound;
}
