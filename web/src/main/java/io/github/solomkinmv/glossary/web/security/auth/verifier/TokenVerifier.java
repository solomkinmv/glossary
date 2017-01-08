package io.github.solomkinmv.glossary.web.security.auth.verifier;

/**
 * Provides ability to verify token by its JTI
 */
public interface TokenVerifier {

    /**
     * Checks if JTI is allowed and valid.
     *
     * @param jti the token JTI
     * @return boolean result of check
     */
    boolean verify(String jti);
}
