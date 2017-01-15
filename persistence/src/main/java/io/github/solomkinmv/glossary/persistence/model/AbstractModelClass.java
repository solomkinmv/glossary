package io.github.solomkinmv.glossary.persistence.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * Abstract class for all models.
 */
@MappedSuperclass
@Getter
@Setter
@ToString
@EqualsAndHashCode
abstract class AbstractModelClass implements ModelObject {
    @Id
    @GeneratedValue
    private Long id;
}
