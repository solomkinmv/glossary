package io.github.solomkinmv.glossary.persistence.bootstrap;

import io.github.solomkinmv.glossary.persistence.dao.RoleDao;
import io.github.solomkinmv.glossary.persistence.model.Role;
import io.github.solomkinmv.glossary.persistence.model.RoleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

/**
 * Bootstraps data for the persistence layer.
 */
@Component
@Profile("!dev")
@Transactional
public class SpringRolesBootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final RoleDao roleDao;

    @Autowired
    public SpringRolesBootstrap(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        saveRoles();
    }

    private void saveRoles() {
        for (RoleType roleType : RoleType.values()) {
            ensureRole(roleType);
        }
    }

    private void ensureRole(RoleType roleType) {
        if (!roleDao.findByRoleType(roleType).isPresent()) {
            roleDao.create(new Role(roleType));
        }
    }
}
