package io.github.solomkinmv.glossary.web.security.endpoint;

import io.github.solomkinmv.glossary.persistence.model.User;
import io.github.solomkinmv.glossary.web.security.config.WebSecurityConfig;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import io.github.solomkinmv.glossary.web.security.model.JsonWebToken;
import io.github.solomkinmv.glossary.web.security.model.LoginRequest;
import io.github.solomkinmv.glossary.web.security.util.JwtTokenFactory;
import io.github.solomkinmv.glossary.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents endpoint for the login request.
 */
@RestController
public class LoginEndpoint {

    private final UserService userService;
    private final JwtTokenFactory tokenFactory;

    @Autowired
    public LoginEndpoint(UserService userService, JwtTokenFactory tokenFactory) {
        this.userService = userService;
        this.tokenFactory = tokenFactory;
    }

    @RequestMapping(value = WebSecurityConfig.FORM_BASED_LOGIN_ENTRY_POINT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> login(@RequestBody LoginRequest loginRequest) {
        // TODO: use validation
        String username = loginRequest.getUsername();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(loginRequest.getPassword())) {
            throw new AuthenticationServiceException("Username or password not provided");
        }

        User user = userService.getByUsername(username)
                               .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        List<GrantedAuthority> authorities = user.getRoles().stream()
                                                 .map(authority -> new SimpleGrantedAuthority(
                                                         authority.getRole().authority()))
                                                 .collect(Collectors.toList());

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(username, authorities);

        JsonWebToken accessToken = tokenFactory.createAccessJwtToken(authenticatedUser);
        JsonWebToken refreshToken = tokenFactory.createRefreshToken(authenticatedUser);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", accessToken.getRawToken());
        tokenMap.put("refreshToken", refreshToken.getRawToken());

        return tokenMap;
    }
}
