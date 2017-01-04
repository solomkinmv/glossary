package io.github.solomkinmv.glossary.web.security.auth.jwt.extractor;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
public interface TokenExtractor {
    String extract(String payload);
}
