package io.github.solomkinmv.glossary.web.security.auth;

import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import io.github.solomkinmv.glossary.web.security.model.JsonWebToken;
import io.github.solomkinmv.glossary.web.security.model.JwtAuthenticationToken;
import io.github.solomkinmv.glossary.web.security.util.JwtParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of {@link AuthenticationProvider} for the JWT authentication.
 */
@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtParser jwtParser;

    @Autowired
    public JwtAuthenticationProvider(JwtParser jwtParser) {
        this.jwtParser = jwtParser;
    }

    /**
     * Authenticates user by specified credentials. This implementation parses raw JWT and
     * creates {@link AuthenticatedUser} using data from the token.
     *
     * @param authentication the authentication object which getObject principal and credentials
     * @return new {@code authentication} object with {@link AuthenticatedUser}
     * and list of {@link GrantedAuthority}
     */
    @Override
    public Authentication authenticate(Authentication authentication) {
        String rawToken = (String) authentication.getCredentials();

        JsonWebToken token = jwtParser.parseToken(rawToken);

        String subject = token.getSubject();
        String scope = token.getScope();

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(scope);

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(subject, authorities);

        return new JwtAuthenticationToken(authenticatedUser, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
