package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.AbstractJpaDaoService;
import io.github.solomkinmv.glossary.persistence.dao.UserDao;
import io.github.solomkinmv.glossary.persistence.model.User;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserDao}.
 */
@Service
public class UserServiceJpaDao extends AbstractJpaDaoService implements UserDao {

    public List<User> listAll() {
        EntityManager entityManager = emf.createEntityManager();

        return entityManager.createQuery("SELECT u FROM User u", User.class).getResultList();
    }

    @Override
    public Optional<User> getById(Long id) {
        EntityManager entityManager = emf.createEntityManager();

        return Optional.ofNullable(entityManager.find(User.class, id));
    }

    @Override
    public User saveOrUpdate(User unsavedUser) {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        User savedUser = entityManager.merge(unsavedUser);
        entityManager.getTransaction().commit();
        return savedUser;
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(User.class, id));
        entityManager.getTransaction().commit();
    }


    @Override
    public Optional<User> findByUsername(String username) {
        EntityManager entityManager = emf.createEntityManager();

        User result = entityManager.createQuery("SELECT u FROM User u WHERE username = :username", User.class)
                                   .setParameter("username", username)
                                   .getSingleResult();
        return Optional.ofNullable(result);
    }

    @Override
    public void deleteAll() {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.createQuery("DELETE FROM User").executeUpdate();
    }
}
