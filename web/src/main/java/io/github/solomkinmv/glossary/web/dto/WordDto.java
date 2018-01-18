package io.github.solomkinmv.glossary.web.dto;

import io.github.solomkinmv.glossary.persistence.model.WordStage;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Null;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordDto {
    @Null
    private Long id;
    private String text;
    private String translation;
    @Null
    private WordStage stage;
    private String image;
    @Null
    private String sound;
}
