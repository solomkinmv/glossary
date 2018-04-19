package io.github.solomkinmv.glossary.words.controller.dto;

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
}
