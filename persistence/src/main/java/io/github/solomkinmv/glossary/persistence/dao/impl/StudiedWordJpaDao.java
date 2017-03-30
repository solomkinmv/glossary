package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.StudiedWordDao;
import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link StudiedWordDao}.
 */
@Repository
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
}
