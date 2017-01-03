package io.github.solomkinmv.glossary.web.security.util;

import io.github.solomkinmv.glossary.web.security.config.JwtSettings;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import io.github.solomkinmv.glossary.web.security.model.Scopes;
import io.github.solomkinmv.glossary.web.security.model.token.AccessJwtToken;
import io.github.solomkinmv.glossary.web.security.model.token.JwtToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
@Component
public class JwtTokenFactory {
    private final JwtSettings settings;

    @Autowired
    public JwtTokenFactory(JwtSettings settings) {
        this.settings = settings;
    }

    /* TODO: use template method pattern*/
    public AccessJwtToken createAccessJwtToken(AuthenticatedUser authenticatedUser) {
        if (StringUtils.isEmpty(authenticatedUser.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT without username");
        }

        if (authenticatedUser.getAuthorities() == null || authenticatedUser.getAuthorities().isEmpty()) {
            throw new IllegalArgumentException("User doesn't have any privileges");
        }

        Claims claims = Jwts.claims().setSubject(authenticatedUser.getUsername());
        claims.put("scopes",
                authenticatedUser.getAuthorities().stream().map(Object::toString).collect(Collectors.toList()));

        Instant currentTime = Instant.now();
        Instant tokenExpirationTime = currentTime.plus(settings.getTokenExpirationTime(), ChronoUnit.MINUTES);

        String token = Jwts.builder()
                           .setClaims(claims)
                           .setIssuer(settings.getTokenIssuer())
                           .setIssuedAt(Date.from(currentTime))
                           .setExpiration(Date.from(tokenExpirationTime))
                           .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                           .compact();

        return new AccessJwtToken(token, claims);
    }

    public JwtToken createRefreshToken(AuthenticatedUser authenticatedUser) {
        if (StringUtils.isEmpty(authenticatedUser.getUsername())) {
            throw new IllegalArgumentException("Cannot create JWT without username");
        }

        Claims claims = Jwts.claims().setSubject(authenticatedUser.getUsername());
        claims.put("scopes", Collections.singletonList(Scopes.REFRESH_TOKEN.authority()));

        Instant currentTime = Instant.now();
        Instant tokenExpirationTime = currentTime.plus(settings.getRefreshTokenExpTime(), ChronoUnit.MINUTES);

        String token = Jwts.builder()
                           .setClaims(claims)
                           .setIssuer(settings.getTokenIssuer())
                           .setId(UUID.randomUUID().toString())
                           .setIssuedAt(Date.from(currentTime))
                           .setExpiration(Date.from(tokenExpirationTime))
                           .signWith(SignatureAlgorithm.HS512, settings.getTokenSigningKey())
                           .compact();

        return new AccessJwtToken(token, claims);
    }
}
