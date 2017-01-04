package io.github.solomkinmv.glossary.web.security.auth.jwt;

import io.github.solomkinmv.glossary.web.security.config.JwtSettings;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import io.github.solomkinmv.glossary.web.security.model.JwtAuthenticationToken;
import io.github.solomkinmv.glossary.web.security.model.token.RawAccessJwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by max on 04.01.17.
 * TODO: add JavaDoc
 */
@Component
public class JwtAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
    private final JwtSettings jwtSettings;

    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username,
                                       UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        RawAccessJwt rawAccessJwt = (RawAccessJwt) authentication.getCredentials();

        // FIXME: the same operations in RefreshTokenEndpoint done using RefreshJwt
        Jws<Claims> claims = rawAccessJwt.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = claims.getBody().getSubject();
        String scopes = claims.getBody().get("scopes", String.class);

        List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(scopes);

        return new AuthenticatedUser(subject, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
