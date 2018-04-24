package io.github.solomkinmv.glossary.words.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solomkinmv.glossary.words.persistence.domain.Word;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import lombok.Value;

@Value
public class WordResponse {
    long id;
    String text;
    String translation;
    WordStage stage;
    String image;
    String sound;

    @JsonCreator
    public WordResponse(@JsonProperty("id") long id,
                        @JsonProperty("text") String text,
                        @JsonProperty("translation") String translation,
                        @JsonProperty("stage") WordStage stage,
                        @JsonProperty("image") String image,
                        @JsonProperty("sound") String sound) {
        this.id = id;
        this.text = text;
        this.translation = translation;
        this.stage = stage;
        this.image = image;
        this.sound = sound;
    }

    public static WordResponse of(Word word) {
        return new WordResponse(word.getId(), word.getText(), word.getTranslation(),
                                word.getStage(), word.getImage(), word.getSound());
    }
}
