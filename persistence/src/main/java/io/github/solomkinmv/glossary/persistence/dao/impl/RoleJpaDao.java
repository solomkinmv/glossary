package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.RoleDao;
import io.github.solomkinmv.glossary.persistence.model.Role;
import io.github.solomkinmv.glossary.persistence.model.RoleType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static io.github.solomkinmv.glossary.persistence.util.DaoUtils.findOrEmpty;

/**
 * Implementation of {@link RoleDao}.
 */
@Repository
@Slf4j
public class RoleJpaDao extends AbstractJpaDao<Role> implements RoleDao {

    public RoleJpaDao() {
        setClazz(Role.class);
    }

    @Override
    public Optional<Role> findByRoleType(RoleType roleType) {
        return findOrEmpty(
                () -> entityManager.createQuery("SELECT r FROM Role r WHERE r.roleType = :roleType", Role.class)
                                   .setParameter("roleType", roleType)
                                   .getSingleResult());
    }
}