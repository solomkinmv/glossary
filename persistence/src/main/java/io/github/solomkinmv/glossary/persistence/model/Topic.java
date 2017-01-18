package io.github.solomkinmv.glossary.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Topic extends AbstractModelClass {

    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.REMOVE})
    @JoinTable
    private List<Word> words;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Topic topic = (Topic) o;
        return Objects.equals(name, topic.name) &&
                Objects.equals(description, topic.description) &&
                wordsEquals(words, topic.words);
    }

    /* Need to override equals method, because Hibernate supplies PersistentBag
    list implementation, which checks only links. */
    private boolean wordsEquals(List<Word> thisWords, List<Word> thatWords) {
        if (thisWords.size() != thatWords.size()) {
            return false;
        }

        for (int i = 0; i < thisWords.size(); i++) {
            if (!thisWords.get(i).equals(thatWords.get(i))) {
                return false;
            }
        }

        return true;
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
        final Object $words = this.getWords();
        result = result * PRIME + ($words == null ? 43 : $words.hashCode());
        return result;
    }
}