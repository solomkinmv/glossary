package io.github.solomkinmv.glossary.web.security.util;

import io.github.solomkinmv.glossary.web.security.config.JwtSettings;
import io.github.solomkinmv.glossary.web.security.exception.JwtExpiredException;
import io.github.solomkinmv.glossary.web.security.model.JsonWebToken;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Component;

/**
 * Created by max on 04.01.17.
 * TODO: add JavaDoc
 */
@Component
public class JwtParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtParser.class);
    private final JwtSettings jwtSettings;

    @Autowired
    public JwtParser(JwtSettings jwtSettings) {
        this.jwtSettings = jwtSettings;
    }

    public JsonWebToken parseToken(String rawToken) {
        try {
            Claims claims = Jwts.parser()
                                .setSigningKey(jwtSettings.getTokenSigningKey())
                                .parseClaimsJws(rawToken)
                                .getBody();

            return new JsonWebToken(rawToken, claims);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException e) {
            LOGGER.error("Invalid JWT", e);
            throw new BadCredentialsException("Invalid JWT", e);
        } catch (ExpiredJwtException e) {
            LOGGER.info("JWT is expired", e);
            throw new JwtExpiredException("JWT expired", e, rawToken);
        }
    }
}
