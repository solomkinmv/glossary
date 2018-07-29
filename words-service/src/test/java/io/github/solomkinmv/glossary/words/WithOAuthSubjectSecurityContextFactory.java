package io.github.solomkinmv.glossary.words;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.testcontainers.shaded.com.google.common.collect.ImmutableMap;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
public class WithOAuthSubjectSecurityContextFactory implements WithSecurityContextFactory<WithOAuthSubject> {

    public static AtomicLong uniqueCounter = new AtomicLong();
    @Autowired
    private AccessTokenConverter accessTokenConverter;

    @Override
    public SecurityContext createSecurityContext(WithOAuthSubject withOAuthSubject) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        // Copy of response from https://myidentityserver.com/identity/connect/accesstokenvalidation
        String subjectId = withOAuthSubject.subjectId();
        if (withOAuthSubject.unique()) {
            subjectId += uniqueCounter.incrementAndGet();
        }
        Map<String, ?> remoteToken = ImmutableMap.<String, Object>builder()
                .put("iss", "https://myfakeidentity.example.com/identity")
                .put("aud", "oauth2-resource")
                .put("exp", OffsetDateTime.now().plusDays(1L).toEpochSecond() + "")
                .put("nbf", OffsetDateTime.now().plusDays(1L).toEpochSecond() + "")
                .put("client_id", "my-client-id")
                .put("scope", Arrays.asList(withOAuthSubject.scopes()))
                .put("sub", subjectId)
                .put("auth_time", OffsetDateTime.now().toEpochSecond() + "")
                .put("idp", "idsrv")
                .put("amr", "password")
                .build();

        OAuth2Authentication authentication = accessTokenConverter.extractAuthentication(remoteToken);
        context.setAuthentication(authentication);
        return context;
    }
}
