package io.github.solomkinmv.glossary.web.security.endpoint;

import io.github.solomkinmv.glossary.persistence.model.User;
import io.github.solomkinmv.glossary.web.security.auth.extractor.TokenExtractor;
import io.github.solomkinmv.glossary.web.security.auth.verifier.TokenVerifier;
import io.github.solomkinmv.glossary.web.security.config.WebSecurityConfig;
import io.github.solomkinmv.glossary.web.security.exception.InvalidJwt;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import io.github.solomkinmv.glossary.web.security.model.JsonWebToken;
import io.github.solomkinmv.glossary.web.security.model.Scopes;
import io.github.solomkinmv.glossary.web.security.util.JwtParser;
import io.github.solomkinmv.glossary.web.security.util.JwtTokenFactory;
import io.github.solomkinmv.glossary.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by max on 03.01.17.
 * TODO: add JavaDoc
 */
@RestController
public class RefreshTokenEndpoint {
    private final JwtTokenFactory jwtTokenFactory;
    private final UserService userService;
    private final TokenVerifier tokenVerifier;
    private final TokenExtractor tokenExtractor;
    private final JwtParser jwtParser;

    @Autowired
    public RefreshTokenEndpoint(JwtTokenFactory jwtTokenFactory, UserService userService,
                                TokenVerifier tokenVerifier, TokenExtractor tokenExtractor, JwtParser jwtParser) {
        this.jwtTokenFactory = jwtTokenFactory;
        this.userService = userService;
        this.tokenVerifier = tokenVerifier;
        this.tokenExtractor = tokenExtractor;
        this.jwtParser = jwtParser;
    }

    @RequestMapping(value = WebSecurityConfig.TOKEN_REFRESH_ENTRY_POINT, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String tokenPayload = tokenExtractor.extract(request.getHeader(WebSecurityConfig.JWT_TOKEN_HEADER_PARAM));

        JsonWebToken jwt = jwtParser.parseToken(tokenPayload);

        if (!(tokenVerifier.verify(jwt.getJti()) && jwt.getScope().contains(Scopes.REFRESH_TOKEN.authority()))) {
            throw new InvalidJwt();
        }

        String subject = jwt.getSubject();
        User user = userService.getByUsername(subject)
                               .orElseThrow(() -> new UsernameNotFoundException("User not found: " + subject));

        if (user.getRoles() == null) {
            throw new InsufficientAuthenticationException("User has no roles assigned");
        }
        List<GrantedAuthority> authorities = user.getRoles().stream()
                                                 .map(authority -> new SimpleGrantedAuthority(
                                                         authority.getRole().authority()))
                                                 .collect(Collectors.toList());

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(user.getUsername(), authorities);

        return Collections.singletonMap("token", jwtTokenFactory.createRefreshToken(authenticatedUser).getRawToken());
    }
}
