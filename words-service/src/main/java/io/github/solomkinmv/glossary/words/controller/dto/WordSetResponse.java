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
    private List<StudiedWordResponse> studiedWords;

    public static WordSetResponse of(WordSet wordSet) {
        return new WordSetResponse(wordSet.getId(), wordSet.getName(), wordSet.getDescription(),
                                   wordSet.getStudiedWords().stream()
                                          .map(StudiedWordResponse::of)
                                          .collect(Collectors.toList()));
    }

    @JsonCreator
    public WordSetResponse(@JsonProperty("id") long id,
                           @JsonProperty("name") String name,
                           @JsonProperty("description") String description,
                           @JsonProperty("studiedWords") List<StudiedWordResponse> studiedWords) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.studiedWords = studiedWords;
    }
}
