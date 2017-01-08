package io.github.solomkinmv.glossary.web.security.auth.extractor;

/**
 * Provides ability to extract token from the payload.
 */
public interface TokenExtractor {

    /**
     * Extracts raw token from the payload.
     *
     * @param payload the payload
     * @return the token
     */
    String extract(String payload);
}
