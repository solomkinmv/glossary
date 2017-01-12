package io.github.solomkinmv.glossary.service.domain;

import io.github.solomkinmv.glossary.persistence.model.Role;
import io.github.solomkinmv.glossary.persistence.model.RoleType;

/**
 * Describes methods to interact with {@link Role}.
 */
public interface RoleService extends CRUDService<Role, Long> {
    Role getByRoleType(RoleType roleType);
}
