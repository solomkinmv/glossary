package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.StudiedWordDao;
import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.solomkinmv.glossary.persistence.util.DaoUtils.findOrEmpty;

/**
 * Implementation of {@link StudiedWordDao}.
 */
@Repository
@Slf4j
public class StudiedWordJpaDao extends AbstractJpaDao<StudiedWord> implements StudiedWordDao {

    public StudiedWordJpaDao() {
        setClazz(StudiedWord.class);
    }

    @Override
    public List<StudiedWord> listAllByUsername(String username) {
        return entityManager.createQuery("SELECT s FROM UserDictionary u " +
                                                 "JOIN u.wordSets w " +
                                                 "JOIN w.studiedWords s " +
                                                 "WHERE u.user.username=:username",
                                         StudiedWord.class)
                            .setParameter("username", username)
                            .getResultList();
    }

    @Override
    public Optional<StudiedWord> findByIdAndUsername(long wordId, String username) {
        return findOrEmpty(() -> entityManager.createQuery("SELECT s FROM UserDictionary u " +
                                                                   "JOIN u.wordSets w " +
                                                                   "JOIN w.studiedWords s " +
                                                                   "WHERE u.user.username=:username AND s.id=:id",
                                                           StudiedWord.class)
                                              .setParameter("id", wordId)
                                              .setParameter("username", username)
                                              .getSingleResult());
    }
}
