package io.github.solomkinmv.glossary.web.security.model;

import io.jsonwebtoken.Claims;

/**
 * Created by max on 04.01.17.
 * TODO: add JavaDoc
 */
public class JsonWebToken {
    private final String rawToken;
    private final Claims claims;

    public JsonWebToken(String rawToken, Claims claims) {
        this.rawToken = rawToken;
        this.claims = claims;
    }

    public String getJti() {
        return claims.getId();
    }

    public String getSubject() {
        return claims.getSubject();
    }

    public String getRawToken() {
        return rawToken;
    }

    public String getScope() {
        return claims.get("scopes", String.class);
    }

    public Claims getClaims() {
        return claims;
    }
}
