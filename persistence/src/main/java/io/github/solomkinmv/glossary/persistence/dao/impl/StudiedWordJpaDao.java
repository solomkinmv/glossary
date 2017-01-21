package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.StudiedWordDao;
import io.github.solomkinmv.glossary.persistence.model.StudiedWord;
import org.springframework.stereotype.Repository;

/**
 * Implementation of {@link StudiedWordDao}.
 */
@Repository
public class StudiedWordJpaDao extends AbstractJpaDao<StudiedWord> implements StudiedWordDao {

    public StudiedWordJpaDao() {
        setClazz(StudiedWord.class);
    }
}
