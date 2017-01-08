package io.github.solomkinmv.glossary.web.security.endpoint;

import io.github.solomkinmv.glossary.web.security.util.JwtTokenFactory;
import io.github.solomkinmv.glossary.web.service.UserService;
import io.jsonwebtoken.lang.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RestController;

/**
 * Represents endpoint to register new user.
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

    /*@RequestMapping(value = WebSecurityConfig.FORM_BASED_REGISTER_ENTRY_POINT, method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> register(@RequestBody RegistrationRequest registrationRequest) {
        String pass = passwordEncoder.encode(registrationRequest.getPassword());
        User user = new User(0L, registrationRequest.getUsername(), pass,
                Collections.singletonList(new Role(2L, RoleType.USER)));
        user.setEmail(registrationRequest.getDetails());

        userService.addUser(user);

        List<GrantedAuthority> authorities = user.getRoles().stream()
                                                 .map(authority -> new SimpleGrantedAuthority(
                                                         authority.getRoleType().authority()))
                                                 .collect(Collectors.toList());

        AuthenticatedUser authenticatedUser = new AuthenticatedUser(registrationRequest.getUsername(), authorities);

        JsonWebToken accessToken = tokenFactory.createAccessJwtToken(authenticatedUser);
        JsonWebToken refreshToken = tokenFactory.createRefreshToken(authenticatedUser);

        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("token", accessToken.getRawToken());
        tokenMap.put("refreshToken", refreshToken.getRawToken());

        return tokenMap;
    }*/
}
