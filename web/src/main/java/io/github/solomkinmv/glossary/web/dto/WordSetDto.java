package io.github.solomkinmv.glossary.web.dto;

import lombok.Value;

import java.util.List;

@Value
public class WordSetDto {
    Long id;
    String name;
    String description;
    List<WordDto> words;
}
