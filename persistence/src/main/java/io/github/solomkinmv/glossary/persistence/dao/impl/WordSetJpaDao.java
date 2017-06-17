package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordSetDao;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.solomkinmv.glossary.persistence.util.DaoUtils.findOrEmpty;

/**
 * Implementation of {@link WordSetDao}.
 */
@Repository
@Slf4j
public class WordSetJpaDao extends AbstractJpaDao<WordSet> implements WordSetDao {

    public WordSetJpaDao() {
        setClazz(WordSet.class);
    }

    @Override
    public List<WordSet> listByUsername(String username) {
        return entityManager.createQuery(
                "SELECT w FROM WordSet w " +
                        "" +
                        "WHERE w.userDictionary.user.username = :username", WordSet.class)
                            .setParameter("username", username)
                            .getResultList();
    }

    @Override
    public Optional<WordSet> findByIdAndUsername(long id, String username) {
        return findOrEmpty(() -> entityManager.createQuery(
                "SELECT w FROM WordSet w " +
                        "WHERE w.userDictionary.user.username = :username AND w.id = :id", WordSet.class)
                                              .setParameter("id", id)
                                              .setParameter("username", username)
                                              .getSingleResult());
    }

    @Override
    public void deleteByIdAndUsername(long id, String username) {
        findByIdAndUsername(id, username).ifPresent(entityManager::remove);
    }
}
