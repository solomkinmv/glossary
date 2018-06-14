package io.github.solomkinmv.glossary.words.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
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
}