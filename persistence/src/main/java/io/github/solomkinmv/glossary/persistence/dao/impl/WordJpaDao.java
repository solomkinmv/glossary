package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.Word;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Implementation of {@link WordDao}.
 */
@Repository
public class WordJpaDao extends AbstractJpaDao<Word> implements WordDao {

    public WordJpaDao() {
        setClazz(Word.class);
    }

    @Override
    public List<Word> search(String text) {
        String searchPattern = "%" + text + "%";

        return entityManager.createQuery("SELECT w FROM Word w WHERE w.text LIKE :pattern", Word.class)
                            .setParameter("pattern", searchPattern)
                            .getResultList();
    }
}
