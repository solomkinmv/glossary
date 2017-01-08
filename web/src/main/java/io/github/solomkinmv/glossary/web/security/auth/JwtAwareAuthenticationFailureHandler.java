package io.github.solomkinmv.glossary.web.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.solomkinmv.glossary.web.security.exception.AuthMethodNotSupportedException;
import io.github.solomkinmv.glossary.web.security.exception.JwtExpiredException;
import io.github.solomkinmv.glossary.web.security.model.ErrorResponse;
import io.github.solomkinmv.glossary.web.security.util.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Implementation of {@link AuthenticationFailureHandler} for the JWT authentication.
 */
@Component
public class JwtAwareAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper mapper;

    @Autowired
    public JwtAwareAuthenticationFailureHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Handles different types of exceptions.
     *
     * @param request   the HttpRequest object
     * @param response  the HttpResponse object
     * @param exception the Exception object
     * @throws IOException if couldn't write value to the HttpResponse
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        // TODO: use pattern matching or chain of responsibilities
        if (exception instanceof BadCredentialsException) {
            mapper.writeValue(response.getWriter(),
                    ErrorResponse.of("Invalid username or password", ErrorCode.AUTHENTICATION,
                            HttpStatus.UNAUTHORIZED));
        } else if (exception instanceof JwtExpiredException) {
            mapper.writeValue(response.getWriter(),
                    ErrorResponse.of("Token has expired", ErrorCode.JWT_TOKEN_EXPIRED, HttpStatus.UNAUTHORIZED));
        } else if (exception instanceof AuthMethodNotSupportedException) {
            mapper.writeValue(response.getWriter(),
                    ErrorResponse.of(exception.getMessage(), ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
        }

        mapper.writeValue(response.getWriter(),
                ErrorResponse.of("Authentication failed", ErrorCode.AUTHENTICATION, HttpStatus.UNAUTHORIZED));
    }
}
