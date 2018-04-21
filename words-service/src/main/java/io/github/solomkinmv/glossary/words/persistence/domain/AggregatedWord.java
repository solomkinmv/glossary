package io.github.solomkinmv.glossary.words.persistence.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregatedWord {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String text;

    @ElementCollection
    private Set<String> translations;

    @ElementCollection
    private Set<String> images;

    private String sound;
}