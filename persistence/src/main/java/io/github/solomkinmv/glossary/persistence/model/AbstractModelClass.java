package io.github.solomkinmv.glossary.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract class for all models.
 */
@MappedSuperclass
@Getter
@Setter
abstract class AbstractModelClass implements ModelObject {
    @Id
    @GeneratedValue
    private Long id;
}
