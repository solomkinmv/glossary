package io.github.solomkinmv.glossary.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Topic extends AbstractModelClass {

    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable
    private List<Word> words;
}