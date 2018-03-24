package io.github.solomkinmv.glossary.tts.service;

/**
 * Describes operations to get speech records URL.
 */
public interface SpeechService {

    /**
     * Returns URL for the speech record.
     *
     * @param speech text to generate speech record
     * @return URL for the speech record audio file
     */
    String getSpeechRecord(String speech);
}
