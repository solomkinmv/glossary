package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.UserDictionaryDao;
import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static io.github.solomkinmv.glossary.persistence.util.DaoUtils.findOrEmpty;

/**
 * Implementation of {@link UserDictionaryDao}.
 */
@Repository
public class UserDictionaryJpaDao extends AbstractJpaDao<UserDictionary> implements UserDictionaryDao {

    public UserDictionaryJpaDao() {
        setClazz(UserDictionary.class);
    }

    @Override
    public Optional<UserDictionary> findByUsername(String username) {
        return findOrEmpty(() -> entityManager.createQuery(
                "SELECT u FROM UserDictionary u WHERE u.user.username = :username", UserDictionary.class)
                                              .setParameter("username", username)
                                              .getSingleResult());
    }
}
