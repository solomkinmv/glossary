package io.github.solomkinmv.glossary.words.util;

import io.github.solomkinmv.glossary.words.config.SecurityConfiguration;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OAuth2Utils {

    public static String subjectId(OAuth2Authentication authentication) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) authentication.getDetails();
        return ((SecurityConfiguration.TokenDetails) details.getDecodedDetails()).getSub();
    }
}
