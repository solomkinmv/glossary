package io.github.solomkinmv.glossary.persistence.util;

import lombok.extern.slf4j.Slf4j;

import javax.persistence.NoResultException;
import java.util.Optional;

@Slf4j
public final class DaoUtils {
    public static <T> Optional<T> findOrEmpty(final DaoRetriever<T> retriever) {
        try {
            return Optional.of(retriever.retrieve());
        } catch (NoResultException ex) {
            log.debug("Can't retrieve results");
        }
        return Optional.empty();
    }
}
