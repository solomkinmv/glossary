package io.github.solomkinmv.glossary.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Word extends AbstractModelClass {

    @NotBlank
    private String word;

    @NotBlank
    private String translation;
}