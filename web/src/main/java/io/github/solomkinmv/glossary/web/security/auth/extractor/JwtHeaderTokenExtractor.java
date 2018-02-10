package io.github.solomkinmv.glossary.web.security.auth.extractor;

import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * Implementation of {@link TokenExtractor} for the JWT.
 * Extracts raw token from the HTTP header.
 */
@Component
public class JwtHeaderTokenExtractor implements TokenExtractor {
    private static final String HEADER_PREFIX = "Bearer ";

    /**
     * Extracts raw token from the HTTP header.
     * Returns substring after the {@code "Bearer "} header prefix.
     *
     * @return the raw token
     */
    @Override
    public String extract(String header) {
        if (StringUtils.isEmpty(header)) {
            throw new AuthenticationServiceException("Authorization header cannot be blank");
        }

        if (header.length() < HEADER_PREFIX.length()) {
            throw new AuthenticationServiceException("Invalid authorization header size");
        }

        return header.substring(HEADER_PREFIX.length(), header.length());
    }
}
