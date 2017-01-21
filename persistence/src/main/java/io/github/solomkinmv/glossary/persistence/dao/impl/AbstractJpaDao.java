package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.CRUDDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Abstract class for all JPA DAO services.
 */
public abstract class AbstractJpaDao<T extends Serializable> implements CRUDDao<T, Long> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractJpaDao.class);

    @PersistenceContext
    EntityManager entityManager;

    private Class<T> clazz;

    final void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Optional<T> findOne(Long id) {
        return Optional.ofNullable(entityManager.find(clazz, id));
    }

    @Override
    public List<T> listAll() {
        return entityManager.createQuery("FROM  " + clazz.getName(), clazz)
                            .getResultList();
    }

    @Override
    public void create(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public T update(T entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void delete(Long id) {
        T entity = entityManager.find(clazz, id);
        if (entity == null) {
            LOGGER.error("No such entity in the DB");
            throw new EntityNotFoundException(clazz.getSimpleName() + " id: " + id);
        }
        entityManager.remove(entity);
    }

    @Override
    public void deleteAll() {
        entityManager.createQuery("DELETE FROM " + clazz.getName()).executeUpdate();
    }
}
