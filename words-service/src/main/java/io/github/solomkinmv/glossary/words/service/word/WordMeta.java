package io.github.solomkinmv.glossary.words.service.word;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
public class WordMeta {

    @NotBlank
    private String text;

    @NotBlank
    private String translation;

    private String image;

    @JsonCreator
    public WordMeta(@JsonProperty("text") @NotBlank String text,
                    @JsonProperty("translation") @NotBlank String translation,
                    @JsonProperty("image") String image) {
        this.text = text;
        this.translation = translation;
        this.image = image;
    }
}
