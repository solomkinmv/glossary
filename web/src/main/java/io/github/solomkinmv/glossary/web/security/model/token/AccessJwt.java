package io.github.solomkinmv.glossary.web.security.model.token;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.jsonwebtoken.Claims;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
public class AccessJwt implements Jwt {
    private final String rawToken;
    @JsonIgnore
    private final Claims claims;

    public AccessJwt(String rawToken, Claims claims) {
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
