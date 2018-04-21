package io.github.solomkinmv.glossary.words.service.wordset;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class WordSetMeta {

    @NonNull
    private long userId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @JsonCreator
    public WordSetMeta(@JsonProperty("userId") long userId,
                       @JsonProperty("name") @NotBlank String name,
                       @JsonProperty("description") @NotBlank String description) {
        this.userId = userId;
        this.name = name;
        this.description = description;
    }
}
