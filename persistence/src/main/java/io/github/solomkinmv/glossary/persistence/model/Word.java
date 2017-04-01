package io.github.solomkinmv.glossary.persistence.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import java.util.List;

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
    private List<String> translations;

    @ElementCollection
    private List<String> images;

    private String sound;
}