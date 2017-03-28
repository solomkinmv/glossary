package io.github.solomkinmv.glossary.persistence.model;

import lombok.*;

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
@AllArgsConstructor
@NoArgsConstructor
abstract class AbstractModelClass implements ModelObject {
    @Id
    @GeneratedValue
    private Long id;
}
