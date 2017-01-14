package io.github.solomkinmv.glossary.service.domain.impl;

import io.github.solomkinmv.glossary.persistence.dao.RoleDao;
import io.github.solomkinmv.glossary.persistence.model.Role;
import io.github.solomkinmv.glossary.persistence.model.RoleType;
import io.github.solomkinmv.glossary.service.domain.RoleService;
import io.github.solomkinmv.glossary.service.exception.DomainObjectAlreadyExistException;
import io.github.solomkinmv.glossary.service.exception.DomainObjectNotFound;
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
    public Optional<Role> getById(Long id) {
        LOGGER.debug("Getting role by id: {}", id);
        return roleDao.getById(id);
    }

    @Override
    public Role save(Role role) {
        LOGGER.debug("Saving role: {}", role);
        if (role.getId() != null) {
            return getById(role.getId())
                    .map(roleDao::saveOrUpdate)
                    .orElseThrow(() -> new DomainObjectAlreadyExistException(
                            "Role with such id is already exist: " + role.getId()));
        }

        return roleDao.saveOrUpdate(role);
    }

    @Override
    public Role update(Role role) {
        LOGGER.debug("Updating role: {}", role);
        if (role.getId() == null) {
            LOGGER.error("Can't update role with null id");
            throw new DomainObjectNotFound("Can't update role with null id");
        }
        return roleDao.saveOrUpdate(role);
    }

    @Override
    public void delete(Long id) {
        LOGGER.debug("Deleting role with id: {}", id);
        roleDao.delete(id);
    }

    @Override
    public Role getByRoleType(RoleType roleType) {
        LOGGER.debug("Getting role by role type: {}", roleType);
        return roleDao.findByRoleType(roleType);
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("Deleting all roles");
        roleDao.deleteAll();
    }
}
