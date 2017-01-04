package io.github.solomkinmv.glossary.web.security.auth.jwt.verifier;

import org.springframework.stereotype.Component;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
@Component
public class BloomFilterTokenVerifier implements TokenVerifier {

    @Override
    public boolean verify(String jti) {
        return true;
    }
}
