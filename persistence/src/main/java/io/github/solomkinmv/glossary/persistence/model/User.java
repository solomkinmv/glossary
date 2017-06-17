package io.github.solomkinmv.glossary.persistence.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.Set;

/**
 * Model, which represents user.
 */
@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractModelClass {

    private String name;
    private String username;
    private String password;
    private String email;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles;
}
