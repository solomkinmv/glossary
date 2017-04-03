package io.github.solomkinmv.glossary.persistence.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"studiedWords", "userDictionary"})
@ToString(callSuper = true, exclude = {"studiedWords", "userDictionary"})
@Slf4j
public class WordSet extends AbstractModelClass {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<StudiedWord> studiedWords;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserDictionary userDictionary;

    public WordSet(String name, String description, List<StudiedWord> studiedWords) {
        this.name = name;
        this.description = description;
        this.studiedWords = studiedWords;
    }

    public WordSet(Long id, String name, String description, List<StudiedWord> studiedWords) {
        super(id);
        this.name = name;
        this.description = description;
        this.studiedWords = studiedWords;
    }
}