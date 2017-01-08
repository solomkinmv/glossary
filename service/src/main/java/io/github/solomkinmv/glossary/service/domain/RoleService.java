package io.github.solomkinmv.glossary.service.domain;

import io.github.solomkinmv.glossary.persistence.model.Role;
import io.github.solomkinmv.glossary.persistence.model.RoleType;

import java.util.List;
import java.util.Optional;

/**
 * Describes methods to interact with {@link Role}.
 */
public interface RoleService {
    List<Role> listAll();

    Optional<Role> getById(long id);

    Role saveOrUpdate(Role role);

    void delete(long id);

    Role getByRoleType(RoleType roleType);
}
