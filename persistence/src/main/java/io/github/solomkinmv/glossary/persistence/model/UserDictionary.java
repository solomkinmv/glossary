package io.github.solomkinmv.glossary.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDictionary extends AbstractModelClass {

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private Set<Topic> topics;

    @OneToOne
    private User user;
}