package io.github.solomkinmv.glossary.web.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.solomkinmv.glossary.web.security.RestAuthenticationEntryPoint;
import io.github.solomkinmv.glossary.web.security.auth.ajax.AjaxAuthenticationProvider;
import io.github.solomkinmv.glossary.web.security.auth.ajax.AjaxLoginProcessingFilter;
import io.github.solomkinmv.glossary.web.security.auth.jwt.JwtAuthenticationProvider;
import io.github.solomkinmv.glossary.web.security.auth.jwt.JwtTokenAuthenticationProcessingFilter;
import io.github.solomkinmv.glossary.web.security.auth.jwt.SkipPathRequestMatcher;
import io.github.solomkinmv.glossary.web.security.auth.jwt.extractor.TokenExtractor;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    public static final String JWT_TOKEN_HEADER_PARAM = "X-Authorization";
    public static final String TOKEN_REFRESH_ENTRY_POINT = "/api/auth/token";
    private static final String FORM_BASED_LOGIN_ENTRY_POINT = "/api/auth/login";
    private static final String TOKEN_BASED_AUTH_ENTRY_POINT = "/api/**";

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;
    private final AjaxAuthenticationProvider ajaxAuthenticationProvider;
    private final JwtAuthenticationProvider jwtAuthenticationProvider;

    private final TokenExtractor tokenExtractor;
    private final ObjectMapper objectMapper;

    @Autowired
    public WebSecurityConfig(RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                             AuthenticationSuccessHandler successHandler,
                             AuthenticationFailureHandler failureHandler,
                             AjaxAuthenticationProvider ajaxAuthenticationProvider,
                             JwtAuthenticationProvider jwtAuthenticationProvider,
                             TokenExtractor tokenExtractor,
                             ObjectMapper objectMapper) {

        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.successHandler = successHandler;
        this.failureHandler = failureHandler;
        this.ajaxAuthenticationProvider = ajaxAuthenticationProvider;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.tokenExtractor = tokenExtractor;
        this.objectMapper = objectMapper;
    }

    private AjaxLoginProcessingFilter buildAjaxLoginProcessingFilter() throws Exception {
        AjaxLoginProcessingFilter filter = new AjaxLoginProcessingFilter(
                FORM_BASED_LOGIN_ENTRY_POINT, successHandler, failureHandler, objectMapper);
        filter.setAuthenticationManager(authenticationManager());
        return filter;
    }

    private JwtTokenAuthenticationProcessingFilter buildJwtTokenAuthenticationProcessingFilter() throws Exception {
        List<String> pathsToSkip = Arrays.asList(TOKEN_REFRESH_ENTRY_POINT, FORM_BASED_LOGIN_ENTRY_POINT);
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
        auth.authenticationProvider(ajaxAuthenticationProvider);
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
            .and()
            .authorizeRequests()
            .antMatchers(TOKEN_BASED_AUTH_ENTRY_POINT).authenticated()
            .and()
            .addFilterBefore(buildAjaxLoginProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(buildJwtTokenAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
