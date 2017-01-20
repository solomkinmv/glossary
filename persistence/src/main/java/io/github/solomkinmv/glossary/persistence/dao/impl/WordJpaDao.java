package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.Word;
import org.springframework.stereotype.Repository;

/**
 * Implementation of {@link WordDao}.
 */
@Repository
public class WordJpaDao extends AbstractJpaDao<Word> implements WordDao {

    public WordJpaDao() {
        setClazz(Word.class);
    }
}
