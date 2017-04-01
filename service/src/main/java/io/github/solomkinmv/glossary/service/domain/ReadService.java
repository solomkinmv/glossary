package io.github.solomkinmv.glossary.service.domain;

import java.util.List;
import java.util.Optional;

/**
 * Created by max on 4/1/17.
 */
public interface ReadService<T, K> {
    List<T> listAll();

    Optional<T> getById(K id);
}
