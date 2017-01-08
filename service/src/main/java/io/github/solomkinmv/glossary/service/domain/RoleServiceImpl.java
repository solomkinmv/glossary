package io.github.solomkinmv.glossary.service.domain;

import io.github.solomkinmv.glossary.persistence.dao.RoleDao;
import io.github.solomkinmv.glossary.persistence.model.Role;
import io.github.solomkinmv.glossary.persistence.model.RoleType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link RoleService}.
 */
@Service
public class RoleServiceImpl implements RoleService {
    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    private final RoleDao roleDao;

    @Autowired
    public RoleServiceImpl(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public List<Role> listAll() {
        LOGGER.debug("Listing all roles");
        return roleDao.listAll();
    }

    @Override
    public Optional<Role> getById(long id) {
        LOGGER.debug("Getting role by id: {}", id);
        return roleDao.getById(id);
    }

    @Override
    public Role saveOrUpdate(Role role) {
        LOGGER.debug("Saving or updating role: {}", role);
        return roleDao.saveOrUpdate(role);
    }

    @Override
    public void delete(long id) {
        LOGGER.debug("Deleting role with id: {}", id);
        roleDao.delete(id);
    }

    @Override
    public Role getByRoleType(RoleType roleType) {
        LOGGER.debug("Getting role by role type: {}", roleType);
        return roleDao.findByRoleType(roleType);
    }
}
