package io.github.solomkinmv.glossary.persistence.bootstrap;

import io.github.solomkinmv.glossary.persistence.dao.RoleDao;
import io.github.solomkinmv.glossary.persistence.dao.UserDao;
import io.github.solomkinmv.glossary.persistence.model.Role;
import io.github.solomkinmv.glossary.persistence.model.RoleType;
import io.github.solomkinmv.glossary.persistence.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Collections;

/**
 * Bootstraps data for the persistence layer.
 */
@Component
public class SpringJPABootstrap implements ApplicationListener<ContextRefreshedEvent> {

    private final UserDao userDao;
    private final RoleDao roleDao;

    @Autowired
    public SpringJPABootstrap(UserDao userDao, RoleDao roleDao) {
        this.userDao = userDao;
        this.roleDao = roleDao;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Role adminRole = roleDao.saveOrUpdate(new Role(RoleType.ADMIN));
        Role userRole = roleDao.saveOrUpdate(new Role(RoleType.USER));

        User user1 = userDao.saveOrUpdate(
                new User("user1", "$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G", "user1@email.com",
                        Collections.singletonList(userRole)));
        User user2 = userDao.saveOrUpdate(
                new User("user2", "$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G", "user2@email.com",
                        Collections.singletonList(userRole)));

        System.out.println();
    }
}
