package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.Word;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static io.github.solomkinmv.glossary.persistence.util.DaoUtils.findOrEmpty;

/**
 * Implementation of {@link WordDao}.
 */
@Repository
@Slf4j
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

    @Override
    public Optional<Word> findByText(String text) {
        return findOrEmpty(() -> entityManager.createQuery("SELECT w FROM Word w WHERE w.text = :text", Word.class)
                                              .setParameter("text", text)
                                              .getSingleResult());
    }
}
