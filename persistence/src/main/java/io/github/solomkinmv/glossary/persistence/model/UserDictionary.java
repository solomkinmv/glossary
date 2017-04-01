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

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable
    private Set<WordSet> wordSets;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;
}