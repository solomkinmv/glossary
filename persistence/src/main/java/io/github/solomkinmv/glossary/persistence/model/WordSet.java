package io.github.solomkinmv.glossary.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class WordSet extends AbstractModelClass {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<StudiedWord> studiedWords;

    public WordSet(Long id, String name, String description, List<StudiedWord> studiedWords) {
        super(id);
        this.name = name;
        this.description = description;
        this.studiedWords = studiedWords;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        WordSet wordSet = (WordSet) o;
        return Objects.equals(name, wordSet.name) &&
                Objects.equals(description, wordSet.description) &&
                wordsEquals(studiedWords, wordSet.studiedWords);
    }

    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * PRIME + super.hashCode();
        final Object $name = this.getName();
        result = result * PRIME + ($name == null ? 43 : $name.hashCode());
        final Object $description = this.getDescription();
        result = result * PRIME + ($description == null ? 43 : $description.hashCode());
        final Object $words = this.getStudiedWords();
        result = result * PRIME + ($words == null ? 43 : $words.hashCode());
        return result;
    }

    /* Need to override equals method, because Hibernate supplies PersistentBag
    list implementation, which checks only links. */
    private boolean wordsEquals(List<StudiedWord> thisStudiedWords, List<StudiedWord> thatStudiedWords) {
        if (thisStudiedWords.size() != thatStudiedWords.size()) {
            return false;
        }

        for (int i = 0; i < thisStudiedWords.size(); i++) {
            if (!thisStudiedWords.get(i).equals(thatStudiedWords.get(i))) {
                return false;
            }
        }

        return true;
    }
}