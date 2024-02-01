package com.crackelets.bigfun.platform.security.middleware;

import com.crackelets.bigfun.platform.security.domain.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserService userService;

    JwtAuthenticationEntryPoint unauthorizedHandler;

    @Bean
    public JwtAuthorizationFilter authorizationFilter() { return new JwtAuthorizationFilter(); }

    @Bean
    public PasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userService);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception { return super.authenticationManagerBean();}

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeHttpRequests().antMatchers(
                        "/api/v1/userss/auth/*",
                        "/api/v1/**",
 /*                       "/api/v1/userss",
                        "/api/v1/attendees",
                        "/api/v1/attendees/*",
                        "/api/v1/events",
                        "/api/v1/events/*",
                        "/api/v1/eventsp",
                        "/api/v1/eventsp/*",
                        "/api/v1/eventsto",
                        "/api/v1/eventsto/*",
                        "/api/v1/payments/*",
                        "/api/v1/organizers",
                        "/api/v1/organizers/*",
                        "/api/v1/organizersto",
                        "/api/v1/organizersto/**",*/
                        "/swagger-ui/**",
                        "/api-docs/**").permitAll()
                .anyRequest().authenticated();
        http.addFilterBefore(authorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}

