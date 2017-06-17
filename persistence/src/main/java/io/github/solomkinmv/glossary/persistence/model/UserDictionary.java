package io.github.solomkinmv.glossary.persistence.model;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserDictionary extends AbstractModelClass {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userDictionary")
    private Set<WordSet> wordSets;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    @PrePersist
    private void addDictionaryToWordSets() {
        wordSets.forEach(wordSet -> wordSet.setUserDictionary(this));
    }

    public void addWordSet(WordSet wordSet) {
        wordSet.setUserDictionary(this);
        wordSets.add(wordSet);
    }

    public void removeWordSet(WordSet wordSet) {
        wordSets.remove(wordSet);
        if (wordSet != null) {
            wordSet.setUserDictionary(null);
        }
    }
}