package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.AbstractJpaDaoService;
import io.github.solomkinmv.glossary.persistence.dao.RoleDao;
import io.github.solomkinmv.glossary.persistence.model.Role;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link RoleDao}.
 */
@Service
public class RoleServiceJpaDao extends AbstractJpaDaoService implements RoleDao {

    @Override
    public List<Role> listAll() {
        EntityManager entityManager = emf.createEntityManager();

        return entityManager.createQuery("SELECT r FROM Role r", Role.class).getResultList();
    }

    @Override
    public Optional<Role> getById(Long id) {
        EntityManager entityManager = emf.createEntityManager();

        return Optional.ofNullable(entityManager.find(Role.class, id));
    }

    @Override
    public Role saveOrUpdate(Role unsavedRole) {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        Role savedRole = entityManager.merge(unsavedRole);
        entityManager.getTransaction().commit();
        return savedRole;
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(Role.class, id));
        entityManager.getTransaction().commit();
    }
}
