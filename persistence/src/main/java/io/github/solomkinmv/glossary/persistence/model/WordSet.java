package io.github.solomkinmv.glossary.persistence.model;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true, exclude = {"words", "userDictionary"})
@ToString(callSuper = true, exclude = {"words", "userDictionary"})
@Slf4j
public class WordSet extends AbstractModelClass {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudiedWord> studiedWords;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
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

    @PreRemove
    private void removeFromUserDictionary() {
        if (userDictionary != null) {
            userDictionary.getWordSets().removeIf(this::equals);
        }
    }
}