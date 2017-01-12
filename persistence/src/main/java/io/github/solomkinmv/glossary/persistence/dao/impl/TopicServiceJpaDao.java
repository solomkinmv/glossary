package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.AbstractJpaDaoService;
import io.github.solomkinmv.glossary.persistence.dao.TopicDao;
import io.github.solomkinmv.glossary.persistence.model.Topic;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link io.github.solomkinmv.glossary.persistence.dao.TopicDao}.
 */
@Service
public class TopicServiceJpaDao extends AbstractJpaDaoService implements TopicDao {

    @Override
    public List<Topic> listAll() {
        EntityManager entityManager = emf.createEntityManager();

        return entityManager.createQuery("SELECT t FROM Topic t", Topic.class)
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
        entityManager.remove(entityManager.find(Topic.class, id));
        entityManager.getTransaction().commit();
    }
}
