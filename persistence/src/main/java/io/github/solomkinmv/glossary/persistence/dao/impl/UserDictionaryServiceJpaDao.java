package io.github.solomkinmv.glossary.persistence.dao.impl;

import io.github.solomkinmv.glossary.persistence.dao.AbstractJpaDaoService;
import io.github.solomkinmv.glossary.persistence.dao.UserDictionaryDao;
import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link UserDictionaryDao}.
 */
@Service
public class UserDictionaryServiceJpaDao extends AbstractJpaDaoService implements UserDictionaryDao {

    @Override
    public List<UserDictionary> listAll() {
        EntityManager entityManager = emf.createEntityManager();

        return entityManager.createQuery("SELECT u FROM UserDictionary u", UserDictionary.class)
                            .getResultList();
    }

    @Override
    public Optional<UserDictionary> getById(Long id) {
        EntityManager entityManager = emf.createEntityManager();

        return Optional.ofNullable(entityManager.find(UserDictionary.class, id));
    }

    @Override
    public UserDictionary saveOrUpdate(UserDictionary unsavedUserDictionary) {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        UserDictionary savedUserDictionary = entityManager.merge(unsavedUserDictionary);
        entityManager.getTransaction().commit();
        return unsavedUserDictionary;
    }

    @Override
    public void delete(Long id) {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.getTransaction().begin();
        entityManager.remove(entityManager.find(UserDictionary.class, id));
        entityManager.getTransaction().commit();
    }

    @Override
    public Optional<UserDictionary> findByUsername(String username) {
        EntityManager entityManager = emf.createEntityManager();

        UserDictionary userDictionary = entityManager.createQuery(
                "SELECT u FROM UserDictionary u WHERE user.username = :username", UserDictionary.class)
                                                     .setParameter("username", username)
                                                     .getSingleResult();
        return Optional.ofNullable(userDictionary);
    }


    @Override
    public void deleteAll() {
        EntityManager entityManager = emf.createEntityManager();

        entityManager.createQuery("DELETE FROM UserDictionary").executeUpdate();
    }
}
