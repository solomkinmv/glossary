package io.github.solomkinmv.glossary.web.security.endpoint;

import io.github.solomkinmv.glossary.persistence.model.Role;
import io.github.solomkinmv.glossary.persistence.model.User;
import io.github.solomkinmv.glossary.persistence.model.UserRole;
import io.github.solomkinmv.glossary.web.dto.RegistrationRequest;
import io.github.solomkinmv.glossary.web.security.config.WebSecurityConfig;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import io.github.solomkinmv.glossary.web.security.model.JsonWebToken;
import io.github.solomkinmv.glossary.web.security.util.JwtTokenFactory;
import io.github.solomkinmv.glossary.web.service.UserService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by max on 05.01.17.
 * TODO: add JavaDoc
 */
@RestController
public class RegisterEndpoint {
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtTokenFactory tokenFactory;

    @Autowired
    public RegisterEndpoint(PasswordEncoder passwordEncoder, UserService userService, JwtTokenFactory tokenFactory) {
        this.tokenFactory = tokenFactory;
        Assert.notNull(passwordEncoder);
        Assert.notNull(userService);
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = WebSecurityConfig.FORM_BASED_REGISTER_ENTRY_POINT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> register(@RequestBody RegistrationRequest registrationRequest) {
        String pass = passwordEncoder.encode(registrationRequest.getPassword());
        User user = new User(0L, registrationRequest.getUsername(), pass,
                Collections.singletonList(new UserRole(2L, Role.USER)));
        user.setDetails(registrationRequest.getDetails());

        userService.addUser(user);

        List<GrantedAuthority> authorities = user.getRoles().stream()
                                                 .map(authority -> new SimpleGrantedAuthority(
                                                         authority.getRole().authority()))
                                                 .collect(Collectors.toList());

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(registrationRequest.getUsername(), authorities);

        JsonWebToken accessToken = tokenFactory.createAccessJwtToken(authenticatedUser);
        JsonWebToken refreshToken = tokenFactory.createRefreshToken(authenticatedUser);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", accessToken.getRawToken());
        tokenMap.put("refreshToken", refreshToken.getRawToken());

        return tokenMap;
    }
}
