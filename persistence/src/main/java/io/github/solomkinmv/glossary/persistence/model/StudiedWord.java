package io.github.solomkinmv.glossary.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToOne;

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

    @OneToOne
    private Word word;
    @Enumerated(EnumType.STRING)
    private WordStage stage;

    public StudiedWord(Word word) {
        this(null, word, WordStage.NOT_LEARNED);
    }

    public StudiedWord(Word word, WordStage stage) {
        this.word = word;
        this.stage = stage;
    }

    public StudiedWord(Long id, Word word, WordStage stage) {
        super(id);
        this.word = word;
        this.stage = stage;
    }
}