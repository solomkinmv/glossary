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

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "userDictionary")
    private Set<WordSet> wordSets;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}