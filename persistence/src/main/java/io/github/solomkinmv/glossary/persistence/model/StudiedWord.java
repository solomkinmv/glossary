package io.github.solomkinmv.glossary.persistence.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.net.URL;

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

    private String text;

    private String translation;

    @Enumerated(EnumType.STRING)
    private WordStage stage;

    private URL image;

    private URL sound;
}