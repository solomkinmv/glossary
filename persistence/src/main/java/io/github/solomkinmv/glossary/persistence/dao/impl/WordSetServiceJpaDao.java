package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordSetDao;
import io.github.solomkinmv.glossary.persistence.model.WordSet;
import org.springframework.stereotype.Repository;

/**
 * Implementation of {@link WordSetDao}.
 */
@Repository
public class WordSetServiceJpaDao extends AbstractJpaDao<WordSet> implements WordSetDao {

    public WordSetServiceJpaDao() {
        setClazz(WordSet.class);
    }
}
