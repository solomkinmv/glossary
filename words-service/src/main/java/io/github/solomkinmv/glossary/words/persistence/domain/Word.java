package io.github.solomkinmv.glossary.words.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Word {
    @Column(unique = true)
    private String text;

    @ElementCollection
    private Set<String> translations;

    @ElementCollection
    private Set<String> images;

    private String sound;
}