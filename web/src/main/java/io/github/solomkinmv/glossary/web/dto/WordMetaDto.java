package io.github.solomkinmv.glossary.web.dto;

import io.github.solomkinmv.glossary.persistence.model.WordStage;
import lombok.Value;

@Value
public class WordMetaDto {
    WordStage stage;
    String image;
}
