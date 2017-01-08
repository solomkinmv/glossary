package io.github.solomkinmv.glossary.persistence.dao;

import io.github.solomkinmv.glossary.persistence.model.Role;
import io.github.solomkinmv.glossary.persistence.model.RoleType;

/**
 * Describes methods to interact with {@link Role} domain object.
 */
public interface RoleDao extends CRUDService<Role, Long> {
    Role findByRoleType(RoleType roleType);
}
