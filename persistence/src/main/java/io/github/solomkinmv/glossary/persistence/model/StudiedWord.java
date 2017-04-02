package io.github.solomkinmv.glossary.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

/**
 * Model for the studied word. Should be unique for each user.
 * <p>Represents learning stage. Could store other custom word information for each user.
 */
@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class StudiedWord extends AbstractModelClass {

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


    public StudiedWord(String text, String translation, WordStage stage) {
        this.text = text;
        this.translation = translation;
        this.stage = stage;
    }

    public StudiedWord(Long id, String text, String translation, WordStage stage) {
        super(id);
        this.text = text;
        this.translation = translation;
        this.stage = stage;
    }
}