package io.github.solomkinmv.glossary.words.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;

import java.util.Map;

@Configuration
@AllArgsConstructor
public class SecurityConfiguration {

    private static final String SUBJECT_KEY = "sub";

    private final ResourceServerProperties resource;

    @Bean
    public AccessTokenConverter accessTokenConverter() {
        return new DefaultAccessTokenConverter() {
            @Override
            public OAuth2Authentication extractAuthentication(Map<String, ?> map) {
                OAuth2Authentication authentication = super.extractAuthentication(map);
                authentication.setDetails(TokenDetails.builder()
                                                      .sub((String) map.get(SUBJECT_KEY))
                                                      .build());
                return authentication;
            }
        };
    }

    @Bean
    public TokenStore jwkTokenStore(AccessTokenConverter accessTokenConverter) {
        return new JwkTokenStore(resource.getJwk().getKeySetUri(), accessTokenConverter);
    }

    @Value
    @Builder
    public static class TokenDetails {
        String sub;
    }
}
