package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.RoleDao;
import io.github.solomkinmv.glossary.persistence.model.Role;
import io.github.solomkinmv.glossary.persistence.model.RoleType;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Implementation of {@link RoleDao}.
 */
@Repository
public class RoleJpaDao extends AbstractJpaDao<Role> implements RoleDao {

    public RoleJpaDao() {
        setClazz(Role.class);
    }

    @Override
    public Optional<Role> findByRoleType(RoleType roleType) {
        Role role = entityManager.createQuery("SELECT r FROM Role r WHERE r.roleType = :roleType", Role.class)
                                 .setParameter("roleType", roleType)
                                 .getSingleResult();
        return Optional.ofNullable(role);
    }
}