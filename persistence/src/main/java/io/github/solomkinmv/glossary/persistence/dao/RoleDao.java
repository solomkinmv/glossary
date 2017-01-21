package io.github.solomkinmv.glossary.persistence.dao;

import io.github.solomkinmv.glossary.persistence.model.Role;
import io.github.solomkinmv.glossary.persistence.model.RoleType;

import java.util.Optional;

/**
 * Describes methods to interact with {@link Role} domain object.
 */
public interface RoleDao extends CRUDDao<Role, Long> {

    Optional<Role> findByRoleType(RoleType roleType);
}
