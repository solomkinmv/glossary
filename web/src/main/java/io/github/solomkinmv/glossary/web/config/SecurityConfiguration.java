package io.github.solomkinmv.glossary.web.config;

import io.github.solomkinmv.glossary.web.security.RestAuthenticationEntryPoint;
import io.github.solomkinmv.glossary.web.security.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final RestAuthenticationEntryPoint restAuthenticationEntryPoint;
    private final SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    public SecurityConfiguration(RestAuthenticationEntryPoint restAuthenticationEntryPoint,
                                 SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler) {
        this.restAuthenticationEntryPoint = restAuthenticationEntryPoint;
        this.authenticationSuccessHandler = authenticationSuccessHandler;
    }

    // todo: create password encoder bean
    // todo: create custom authentication provider
    // todo: use custom authentication manager builder
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            .withUser("temporary").password("temporary").roles("ADMIN").and()
            .withUser("user").password("userPass").roles("USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
            .disable()
            .exceptionHandling()
            .authenticationEntryPoint(restAuthenticationEntryPoint)
            .and()
            .authorizeRequests()
            .antMatchers("/foos")
            .authenticated()
            .and()
            .formLogin()
            .successHandler(authenticationSuccessHandler)
            .failureHandler(new SimpleUrlAuthenticationFailureHandler())
            .and()
            .logout();
    }
}
