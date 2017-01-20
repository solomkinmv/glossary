package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.UserDictionaryDao;
import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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
        UserDictionary userDictionary = entityManager.createQuery(
                "SELECT u FROM UserDictionary u WHERE u.user.username = :username", UserDictionary.class)
                                                     .setParameter("username", username)
                                                     .getSingleResult();
        return Optional.ofNullable(userDictionary);
    }
}
