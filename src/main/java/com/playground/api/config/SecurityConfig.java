package com.playground.api.config;

import com.playground.api.filters.AuthFilter;
import com.playground.api.handlers.CustomAccessDeniedHandler;
import com.playground.api.handlers.CustomAuthErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final AuthFilter authFilter;
    private final CustomAuthErrorHandler customAuthErrorHandler;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Autowired
    public SecurityConfig(
            AuthFilter authFilter,
            CustomAuthErrorHandler customAuthErrorHandler,
            CustomAccessDeniedHandler customAccessDeniedHandler
    ) {
        this.authFilter = authFilter;
        this.customAuthErrorHandler = customAuthErrorHandler;
        this.customAccessDeniedHandler = customAccessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(
                        (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling((exceptionHandling) -> exceptionHandling
                        .authenticationEntryPoint(customAuthErrorHandler)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .authorizeHttpRequests(
                        auth -> auth
                                // Allow access to the authentication endpoints
                                .requestMatchers("/auth/**").permitAll()
                                // Allow access to basic health check endpoint
                                .requestMatchers("/actuator/health").permitAll()
                                // Allow access to Swagger UI and OpenAPI documentation
                                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/docs/**").permitAll()
                                // Require authentication for all other requests
                                .anyRequest().authenticated()
                );

        http.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}