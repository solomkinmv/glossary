package io.github.solomkinmv.glossary.words.controller.dto;

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
}
