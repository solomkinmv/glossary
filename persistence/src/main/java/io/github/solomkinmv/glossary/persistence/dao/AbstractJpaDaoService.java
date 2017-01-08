package io.github.solomkinmv.glossary.persistence.dao;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

/**
 * Abstract class for all JPA DAO services.
 */
public abstract class AbstractJpaDaoService {
    protected EntityManagerFactory emf;

    @PersistenceUnit
    public void setEmf(EntityManagerFactory emf) {
        this.emf = emf;
    }
}
