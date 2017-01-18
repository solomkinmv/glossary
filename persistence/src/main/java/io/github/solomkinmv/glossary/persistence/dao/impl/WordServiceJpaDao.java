package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.AbstractJpaDaoService;
import io.github.solomkinmv.glossary.persistence.dao.WordDao;
import io.github.solomkinmv.glossary.persistence.model.Word;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link WordDao}.
 */
@Service
public class WordServiceJpaDao extends AbstractJpaDaoService implements WordDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(WordServiceJpaDao.class);

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
        Word entity = entityManager.find(Word.class, id);
        if (entity == null) {
            LOGGER.error("No such entity in the DB");
            throw new EntityNotFoundException("Word id: " + id);
        }
        entityManager.remove(entity);
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteAll() {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM Word").executeUpdate();
        entityManager.getTransaction().commit();
    }
}
