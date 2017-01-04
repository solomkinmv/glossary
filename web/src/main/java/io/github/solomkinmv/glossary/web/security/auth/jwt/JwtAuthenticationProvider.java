package io.github.solomkinmv.glossary.web.security.auth.jwt;

import io.github.solomkinmv.glossary.web.security.config.JwtSettings;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import io.github.solomkinmv.glossary.web.security.model.JwtAuthenticationToken;
import io.github.solomkinmv.glossary.web.security.model.token.RawAccessJwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by max on 04.01.17.
 * TODO: add JavaDoc
 */
@Component
@SuppressWarnings("unchecked")
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtSettings jwtSettings;

    @Autowired
    public JwtAuthenticationProvider(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        RawAccessJwt rawAccessJwt = (RawAccessJwt) authentication.getCredentials();

        // FIXME: the same operations in RefreshTokenEndpoint done using RefreshJwt
        Jws<Claims> claims = rawAccessJwt.parseClaims(jwtSettings.getTokenSigningKey());
        String subject = claims.getBody().getSubject();
        List<String> scopes = claims.getBody().get("scopes", List.class);

        List<GrantedAuthority> authorities = scopes.stream()
                                                   .map(authority -> new SimpleGrantedAuthority(authority))
                                                   .collect(Collectors.toList());

        // FIXME: deal with null instead of id
        AuthenticatedUser authenticatedUser = new AuthenticatedUser(null, subject, authorities);

        return new JwtAuthenticationToken(authenticatedUser, authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
