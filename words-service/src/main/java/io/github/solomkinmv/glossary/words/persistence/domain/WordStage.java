package io.github.solomkinmv.glossary.words.persistence.domain;

/**
 * Represents studying stage for studied {@link AggregatedWord}.
 * <p>Used in {@link StudiedWord}.
 */
public enum WordStage {
    NOT_LEARNED,
    LEARNING,
    LEARNED
}
