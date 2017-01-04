package io.github.solomkinmv.glossary.web.security.auth.ajax;

import io.github.solomkinmv.glossary.persistence.model.User;
import io.github.solomkinmv.glossary.web.security.model.AuthenticatedUser;
import io.github.solomkinmv.glossary.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO: add JavaDoc
 * TODO: extend from AbstractUserDetailsAuthenticationProvider
 */
@Component
public class AjaxAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    private final BCryptPasswordEncoder encoder;
    private final UserService userService;

    @Autowired
    public AjaxAuthenticationProvider(BCryptPasswordEncoder encoder, UserService userService) {
        this.encoder = encoder;
        this.userService = userService;
    }

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
                                                  UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
    }

    @Override
    protected UserDetails retrieveUser(String username,
                                       UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        User user = userService.getByUsername(username)
                               .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        String password = (String) authentication.getCredentials();

        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Authentication failed. Username or password not valid");
        }

        if (user.getRoles() == null) {
            throw new InsufficientAuthenticationException("User has not roles assigned");
        }

        List<GrantedAuthority> authorities = user.getRoles().stream()
                                                 .map(authority -> new SimpleGrantedAuthority(
                                                         authority.getRole().authority()))
                                                 .collect(Collectors.toList());

        return new AuthenticatedUser(user.getUsername(), authorities);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
