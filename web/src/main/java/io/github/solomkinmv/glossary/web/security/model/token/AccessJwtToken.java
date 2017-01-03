package io.github.solomkinmv.glossary.web.security.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
public class AccessJwtToken implements JwtToken {
    private final String rawToken;
    @JsonIgnore
    private final Claims claims;

    public AccessJwtToken(String rawToken, Claims claims) {
        this.rawToken = rawToken;
        this.claims = claims;
    }

    public Claims getClaims() {
        return claims;
    }

    @Override
    public String getToken() {
        return rawToken;
    }
}
