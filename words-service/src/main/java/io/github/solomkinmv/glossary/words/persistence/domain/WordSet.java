package io.github.solomkinmv.glossary.words.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class WordSet {

    @Id
    @GeneratedValue
    private Long id;

    @Column
    private long userId;

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudiedWord> studiedWords;

    public WordSet(String name, String description) {
        this.name = name;
        this.description = description;
    }
}