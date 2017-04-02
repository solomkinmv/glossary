package io.github.solomkinmv.glossary.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

/**
 * Model for the studied word. Should be unique for each user.
 * <p>Represents learning stage. Could store other custom word information for each user.
 */
@Entity
@Data
@ToString(callSuper = true, exclude = {"wordSets"})
@EqualsAndHashCode(callSuper = true, exclude = {"wordSets"})
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

    @ManyToMany(mappedBy = "studiedWords")
    private List<WordSet> wordSets;

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

    @PreRemove
    private void remoteWordsFromWordSets() {
        wordSets.forEach(wordSet -> wordSet.getStudiedWords().remove(this));
    }
}