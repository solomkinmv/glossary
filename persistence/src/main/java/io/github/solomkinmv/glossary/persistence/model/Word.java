package io.github.solomkinmv.glossary.persistence.model;

import lombok.*;
import org.hibernate.validator.constraints.NotBlank;

import javax.persistence.Entity;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class Word extends AbstractModelClass {
    @NotBlank
    private String text;

    @NotBlank
    private String translation;

    private String imageUrl;

    public Word(String text, String translation) {
        this.text = text;
        this.translation = translation;
    }
}