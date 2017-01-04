package io.github.solomkinmv.glossary.web.security.model;

import io.github.solomkinmv.glossary.web.security.model.token.RawAccessJwt;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private RawAccessJwt rawAccessJwt;
    private AuthenticatedUser authenticatedUser;

    public JwtAuthenticationToken(RawAccessJwt unsafeToken) {
        super(null);
        this.rawAccessJwt = unsafeToken;
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
        return rawAccessJwt;
    }

    @Override
    public Object getPrincipal() {
        return authenticatedUser;
    }

    @Override
    public void eraseCredentials() {
        super.eraseCredentials();
        rawAccessJwt = null;
    }
}
