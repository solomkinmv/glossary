package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.AbstractJpaDaoService;
import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.Word;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link WordDao}.
 */
@Service
public class WordServiceJpaDao extends AbstractJpaDaoService implements WordDao {

    @Override
    public List<Word> listAll() {
        EntityManager entityManager = emf.createEntityManager();

        return entityManager.createQuery("SELECT w FROM Word w", Word.class)
                            .getResultList();
    }

    @Override
    public Optional<Word> getById(Long id) {
        EntityManager entityManager = emf.createEntityManager();

        return Optional.ofNullable(entityManager.find(Word.class, id));
    }

    @Override
    public Word saveOrUpdate(Word unsavedWord) {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        Word savedWord = entityManager.merge(unsavedWord);
        entityManager.getTransaction().commit();
        return savedWord;
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(Word.class, id));
        entityManager.getTransaction().commit();
    }
}
