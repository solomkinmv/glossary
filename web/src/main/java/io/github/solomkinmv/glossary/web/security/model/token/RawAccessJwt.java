package io.github.solomkinmv.glossary.web.security.model.token;

import io.github.solomkinmv.glossary.web.security.exception.JwtExpiredException;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
public class RawAccessJwt implements Jwt {
    private static final Logger LOGGER = LoggerFactory.getLogger(RawAccessJwt.class);

    private final String token;

    public RawAccessJwt(String token) {
        this.token = token;
    }

    public Jws<Claims> parseClaims(String signingKey) {
        try {
            return Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);
        } catch (UnsupportedJwtException | MalformedJwtException | IllegalArgumentException | SignatureException e) {
            LOGGER.error("Invalid JWT", e);
            throw new BadCredentialsException("Invalid JWT", e);
        } catch (ExpiredJwtException e) {
            LOGGER.info("JWT is expired", e);
            throw new JwtExpiredException("JWT expired", e, this);
        }
    }

    @Override
    public String getToken() {
        return token;
    }
}
