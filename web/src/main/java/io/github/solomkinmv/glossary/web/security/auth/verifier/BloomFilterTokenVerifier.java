package io.github.solomkinmv.glossary.web.security.auth.verifier;

import org.springframework.stereotype.Component;

/**
 * Implementation of {@link TokenVerifier}. This implementation verifies all tokens.
 */
@Component
public class BloomFilterTokenVerifier implements TokenVerifier {

    /**
     * Returns true for any token JTI.
     *
     * @param jti the token JTI
     * @return true
     */
    @Override
    public boolean verify(String jti) {
        return true;
    }
}
