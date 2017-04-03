package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordSetDao;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Optional;

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
                "SELECT w FROM UserDictionary u " +
                        "JOIN u.wordSets w " +
                        "WHERE u.user.username = :username", WordSet.class)
                            .setParameter("username", username)
                            .getResultList();
    }

    @Override
    public Optional<WordSet> findByIdAndUsername(long id, String username) {
        try {
            return Optional.of(entityManager.createQuery(
                    "SELECT w FROM UserDictionary u JOIN u.wordSets w " +
                            "WHERE u.user.username = :username AND w.id = :id", WordSet.class)
                                            .setParameter("id", id)
                                            .setParameter("username", username)
                                            .getSingleResult());
        } catch (NoResultException e) {
            log.debug("No words found for user {} with id {}", username, id);
            return Optional.empty();
        }
    }

    @Override
    public void deleteByIdAndUsername(long id, String username) {
        entityManager.createQuery(
                "DELETE FROM WordSet w " +
                        "WHERE w IN" +
                        "(SELECT ws FROM UserDictionary u " +
                        "JOIN u.wordSets ws " +
                        "WHERE u.user.username = :username AND ws.id = :id)")
                     .setParameter("id", id)
                     .setParameter("username", username)
                     .executeUpdate();
    }
}
