package io.github.solomkinmv.glossary.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordSetMetaDto {
    private String name;
    private String description;
}
