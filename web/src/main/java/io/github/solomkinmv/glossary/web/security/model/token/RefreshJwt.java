package io.github.solomkinmv.glossary.web.security.model.token;

import io.github.solomkinmv.glossary.web.security.model.Scopes;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

import java.util.List;
import java.util.Optional;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 * TODO: do I need this class?
 */
public class RefreshJwt implements Jwt {
    private final Jws<Claims> claims;

    public RefreshJwt(Jws<Claims> claims) {
        this.claims = claims;
    }

    public static Optional<RefreshJwt> create(RawAccessJwt token, String signingKey) {
        Jws<Claims> claims = token.parseClaims(signingKey);

        List scopes = claims.getBody().get("scopes", List.class);


        if (scopes == null || scopes.isEmpty() || !containsRefreshTokenScope(scopes)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshJwt(claims));
    }

    private static boolean containsRefreshTokenScope(List scopes) {
        for (Object scope : scopes) {
            if (Scopes.REFRESH_TOKEN.authority().equals(scope)) {
                return true;
            }
        }

        return false;
    }

    public Jws<Claims> getClaims() {
        return claims;
    }

    @Override
    public String getToken() {
        return null;
    }

    public String getJti() {
        return claims.getBody().getId();
    }

    public String getSubject() {
        return claims.getBody().getSubject();
    }
}
