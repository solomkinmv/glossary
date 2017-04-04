package io.github.solomkinmv.glossary.persistence.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.Set;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Word extends AbstractModelClass {
    @Column(unique = true)
    private String text;

    @ElementCollection
    private Set<String> translations;

    @ElementCollection
    private Set<String> images;

    private String sound;
}