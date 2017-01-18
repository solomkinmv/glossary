package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.AbstractJpaDaoService;
import io.github.solomkinmv.glossary.persistence.dao.TopicDao;
import io.github.solomkinmv.glossary.persistence.model.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link io.github.solomkinmv.glossary.persistence.dao.TopicDao}.
 */
@Service
public class TopicServiceJpaDao extends AbstractJpaDaoService implements TopicDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(TopicServiceJpaDao.class);

    @Override
    public List<Topic> listAll() {
        EntityManager entityManager = emf.createEntityManager();

        return entityManager.createQuery("FROM Topic", Topic.class)
                            .getResultList();
    }

    @Override
    public Optional<Topic> getById(Long id) {
        EntityManager entityManager = emf.createEntityManager();

        return Optional.ofNullable(entityManager.find(Topic.class, id));
    }

    @Override
    public Topic saveOrUpdate(Topic unsavedTopic) {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        Topic savedTopic = entityManager.merge(unsavedTopic);
        entityManager.getTransaction().commit();
        return savedTopic;
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        Topic entity = entityManager.find(Topic.class, id);
        if (entity == null) {
            LOGGER.error("No such entity in the DB");
            throw new EntityNotFoundException("Topic id: " + id);
        }
        entityManager.remove(entityManager.find(Topic.class, id));
        entityManager.getTransaction().commit();
    }

    @Override
    public void deleteAll() {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.createQuery("DELETE FROM Topic").executeUpdate();
        entityManager.getTransaction().commit();
    }
}
