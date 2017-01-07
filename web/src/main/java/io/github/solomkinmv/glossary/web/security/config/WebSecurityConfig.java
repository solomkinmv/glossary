package io.github.solomkinmv.glossary.web.security.config;

import io.github.solomkinmv.glossary.web.security.RestAuthenticationEntryPoint;
import io.github.solomkinmv.glossary.web.security.auth.JwtAuthenticationProvider;
import io.github.solomkinmv.glossary.web.security.auth.JwtTokenAuthenticationProcessingFilter;
import io.github.solomkinmv.glossary.web.security.auth.SkipPathRequestMatcher;
import io.github.solomkinmv.glossary.web.security.auth.extractor.TokenExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

/**
 * Spring Web Security configuration.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";
    public static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";
    public static final String FORM_BASED_REGISTER_ENTRY_POINT = "/api/auth/register";
    private static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final AuthenticationFailureHandler failureHandler;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final TokenExtractor tokenExtractor;

    @Autowired
    public WebSecurityConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                             AuthenticationFailureHandler failureHandler,
                             JwtAuthenticationProvider jwtAuthenticationProvider,
                             TokenExtractor tokenExtractor) {

        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.failureHandler = failureHandler;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.tokenExtractor = tokenExtractor;
    }

    private JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
        List<String> pathsToSkip = Arrays.asList(TOKEN_REFRESH_ENTRY_POINT, FORM_BASED_LOGIN_ENTRY_POINT,
                FORM_BASED_REGISTER_ENTRY_POINT);
        SkipPathRequestMatcher matcher = new SkipPathRequestMatcher(pathsToSkip,
                TOKEN_BASED_AUTH_ENTRY_POINT);

        JwtTokenAuthenticationProcessingFilter filter = new JwtTokenAuthenticationProcessingFilter(
                failureHandler, tokenExtractor, matcher);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
            .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(FORM_BASED_LOGIN_ENTRY_POINT).permitAll()
            .antMatchers(TOKEN_REFRESH_ENTRY_POINT).permitAll()
            .antMatchers(FORM_BASED_REGISTER_ENTRY_POINT).permitAll()
            .and()
            .authorizeRequests()
            .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()
            .and()
            .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
