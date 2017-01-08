package io.github.solomkinmv.glossary.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Model, which represents {@link User user} role.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role extends AbstractModelClass {

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "roles")
    private final List<User> users = new ArrayList<>();

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }

        if (!user.getRoles().contains(this)) {
            user.getRoles().add(this);
        }
    }

    public void removeUser(User user) {
        users.remove(user);
        user.getRoles().remove(this);
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleType=" + roleType +
                '}';
    }
}
