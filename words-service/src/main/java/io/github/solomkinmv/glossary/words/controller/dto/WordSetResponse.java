package io.github.solomkinmv.glossary.words.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solomkinmv.glossary.words.persistence.domain.WordSet;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class WordSetResponse {
    private long id;
    private String name;
    private String description;
    private List<WordResponse> words;

    @JsonCreator
    public WordSetResponse(@JsonProperty("id") long id,
                           @JsonProperty("name") String name,
                           @JsonProperty("description") String description,
                           @JsonProperty("words") List<WordResponse> words) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.words = words;
    }

    public static WordSetResponse of(WordSet wordSet) {
        return new WordSetResponse(wordSet.getId(), wordSet.getName(), wordSet.getDescription(),
                                   wordSet.getWords().stream()
                                          .map(WordResponse::of)
                                          .collect(Collectors.toList()));
    }
}
