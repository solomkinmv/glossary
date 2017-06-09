package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.UserDao;
import io.github.solomkinmv.glossary.persistence.model.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static io.github.solomkinmv.glossary.persistence.util.DaoUtils.findOrEmpty;

/**
 * Implementation of {@link UserDao}.
 */
@Repository
public class UserJpaDao extends AbstractJpaDao<User> implements UserDao {

    public UserJpaDao() {
        setClazz(User.class);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return findOrEmpty(
                () -> entityManager.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                                   .setParameter("username", username)
                                   .getSingleResult());
    }
}
