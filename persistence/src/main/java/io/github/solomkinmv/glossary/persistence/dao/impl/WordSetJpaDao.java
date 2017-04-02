package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordSetDao;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link WordSetDao}.
 */
@Repository
public class WordSetJpaDao extends AbstractJpaDao<WordSet> implements WordSetDao {

    public WordSetJpaDao() {
        setClazz(WordSet.class);
    }

    @Override
    public List<WordSet> listByUsername(String username) {
        return entityManager.createQuery(
                "SELECT w FROM UserDictionary u " +
                        "JOIN u.wordSets w " +
                        "WHERE u.user.username = :username", WordSet.class)
                            .setParameter("username", username)
                            .getResultList();
    }
}
