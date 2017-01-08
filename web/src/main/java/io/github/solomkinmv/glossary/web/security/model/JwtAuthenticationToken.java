package io.github.solomkinmv.glossary.web.security.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Represents JWT authentication token. Used in Spring Security during authentication.
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private String rawToken;
    private AuthenticatedUser authenticatedUser;

    public JwtAuthenticationToken(String rawToken) {
        super(null);
        this.rawToken = rawToken;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(AuthenticatedUser authenticatedUser,
                                  Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.eraseCredentials();
        this.authenticatedUser = authenticatedUser;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return rawToken;
    }

    @Override
    public Object getPrincipal() {
        return authenticatedUser;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        rawToken = null;
    }
}
