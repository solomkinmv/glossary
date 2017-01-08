package io.github.solomkinmv.glossary.web.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implementation of {@link AuthenticationEntryPoint} to sent 401 Unauthorized
 * response if supplied no credentials.
 */
@Component
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * Sends a 401 Unauthorized response because there is no "login page" to redirect to.
     * Invoked when user tries to access a secured REST resource without supplying any credentials.
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.sendError(HttpStatus.UNAUTHORIZED.value(), "Unauthorized");
    }
}
