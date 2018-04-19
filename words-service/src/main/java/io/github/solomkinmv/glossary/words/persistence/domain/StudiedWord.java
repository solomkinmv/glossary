package io.github.solomkinmv.glossary.words.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

/**
 * Model for the studied word. Should be unique for each user.
 * <p>Represents learning stage. Could store other custom word information for each user.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudiedWord {

    @Id
    @GeneratedValue
    private Long id;

    @NotBlank
    private String text;

    @NotBlank
    private String translation;

    @Enumerated(EnumType.STRING)
    private WordStage stage;

    private String image;

    private String sound;

    public StudiedWord(String text, String translation) {
        this(text, translation, WordStage.NOT_LEARNED);
    }

    public StudiedWord(Long id, String text, String translation) {
        this(id, text, translation, WordStage.NOT_LEARNED);
    }

    public StudiedWord(String text, String translation, WordStage stage) {
        this.text = text;
        this.translation = translation;
        this.stage = stage;
    }

    public StudiedWord(Long id, String text, String translation, WordStage stage) {
        this.id = id;
        this.text = text;
        this.translation = translation;
        this.stage = stage;
    }
}