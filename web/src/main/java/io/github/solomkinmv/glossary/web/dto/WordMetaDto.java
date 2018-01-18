package io.github.solomkinmv.glossary.web.dto;

import io.github.solomkinmv.glossary.persistence.model.WordStage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordMetaDto {
    private WordStage stage;
    private String image;
}
