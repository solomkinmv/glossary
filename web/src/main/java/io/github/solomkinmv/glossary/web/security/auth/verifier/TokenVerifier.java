package io.github.solomkinmv.glossary.web.security.auth.verifier;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 * TODO: find out about JTI in JWT
 */
public interface TokenVerifier {
    boolean verify(String jti);
}
