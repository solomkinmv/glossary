package io.github.solomkinmv.glossary.web.dto;

import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.Null;
import java.util.List;

@Value
public class WordSetDto {
    @Null
    Long id;
    String name;
    String description;
    @Valid
    List<WordDto> words;
}
