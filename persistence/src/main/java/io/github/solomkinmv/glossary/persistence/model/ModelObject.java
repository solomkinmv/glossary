package io.github.solomkinmv.glossary.persistence.model;

import java.io.Serializable;

/**
 * Describes methods for all persistence models.
 */
public interface ModelObject extends Serializable {

    Long getId();

    void setId(Long id);
}
