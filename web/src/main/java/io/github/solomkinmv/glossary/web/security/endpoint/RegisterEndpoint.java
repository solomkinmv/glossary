package io.github.solomkinmv.glossary.web.security.endpoint;

import io.github.solomkinmv.glossary.persistence.model.Role;
import io.github.solomkinmv.glossary.persistence.model.RoleType;
import io.github.solomkinmv.glossary.persistence.model.User;
import io.github.solomkinmv.glossary.persistence.model.UserDictionary;
import io.github.solomkinmv.glossary.service.domain.RoleService;
import io.github.solomkinmv.glossary.service.domain.UserDictionaryService;
import io.github.solomkinmv.glossary.web.dto.RegistrationRequest;
import io.github.solomkinmv.glossary.web.security.config.WebSecurityConfig;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import io.github.solomkinmv.glossary.web.security.model.JsonWebToken;
import io.github.solomkinmv.glossary.web.security.util.JwtTokenFactory;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Represents endpoint to register new user.
 */
@RestController
@Slf4j
public class RegisterEndpoint {
    private final PasswordEncoder passwordEncoder;
    private final UserDictionaryService userDictionaryService;
    private final RoleService roleService;
    private final JwtTokenFactory tokenFactory;

    @Autowired
    public RegisterEndpoint(PasswordEncoder passwordEncoder, UserDictionaryService userDictionaryService,
                            RoleService roleService, JwtTokenFactory tokenFactory) {
        this.userDictionaryService = userDictionaryService;
        this.roleService = roleService;
        this.tokenFactory = tokenFactory;
        Assert.notNull(passwordEncoder);
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(value = WebSecurityConfig.FORM_BASED_REGISTER_ENTRY_POINT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> register(@Validated @RequestBody RegistrationRequest request) {
        log.info("Registering user: {}", request);
        String pass = passwordEncoder.encode(request.getPassword());

        Role role = roleService.getByRoleType(RoleType.USER);
        User user = new User(request.getName(), request.getUsername(), pass, request.getEmail(),
                             Collections.singleton(role));

        userDictionaryService.save(new UserDictionary(new HashSet<>(), user));

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(request.getUsername(),
                                                                    Collections.singletonList(
                                                                            new SimpleGrantedAuthority(
                                                                                    RoleType.USER.authority())));

        JsonWebToken accessToken = tokenFactory.createAccessJwtToken(authenticatedUser);
        JsonWebToken refreshToken = tokenFactory.createRefreshToken(authenticatedUser);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", accessToken.getRawToken());
        tokenMap.put("refreshToken", refreshToken.getRawToken());

        return tokenMap;
    }
}
