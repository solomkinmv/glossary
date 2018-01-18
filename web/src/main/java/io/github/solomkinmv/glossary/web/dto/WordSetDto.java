package io.github.solomkinmv.glossary.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordSetDto {
    @Null
    private Long id;
    private String name;
    private String description;
    @Valid
    private List<WordDto> words;
}
