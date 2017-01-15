package io.github.solomkinmv.glossary.persistence.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.OneToOne;

@Entity
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserStatistics extends AbstractModelClass {

    @OneToOne
    private User user;
}
