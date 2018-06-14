package io.github.solomkinmv.glossary.words.controller.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.solomkinmv.glossary.words.persistence.domain.StudiedWord;
import io.github.solomkinmv.glossary.words.persistence.domain.WordStage;
import lombok.Value;

@Value
public class StudiedWordResponse {
    long id;
    String text;
    String translation;
    WordStage stage;
    String image;
    String sound;

    public static StudiedWordResponse of(StudiedWord studiedWord) {
        return new StudiedWordResponse(studiedWord.getId(), studiedWord.getText(), studiedWord.getTranslation(),
                                       studiedWord.getStage(), studiedWord.getImage(), studiedWord.getSound());
    }

    @JsonCreator
    public StudiedWordResponse(@JsonProperty("id") long id,
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
}
