package io.github.solomkinmv.glossary.service.dto;

import io.github.solomkinmv.glossary.persistence.model.WordStage;
import lombok.Value;

import java.net.URL;

@Value
public class WordDto {
    Long id;
    String origin;
    String translation;
    WordStage stage;
    URL image;
    URL sound;
}
