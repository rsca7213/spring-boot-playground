package com.playground.api.filters;

import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.Exception;
import com.playground.api.models.AuthUser;
import com.playground.api.repositories.UserRepository;
import com.playground.api.utils.AuthUserJwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AuthFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final AuthUserJwtUtils authUserJwtUtils;

    @Autowired
    public AuthFilter(UserRepository userRepository, AuthUserJwtUtils authUserJwtUtils) {
        this.userRepository = userRepository;
        this.authUserJwtUtils = authUserJwtUtils;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Extract the JWT auth token from the request header
        String authToken = request.getHeader("Authorization");

        // If the token is not present, continue the filter chain
        if (authToken == null || !authToken.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Remove the "Bearer " prefix from the token
        String token = authToken.substring(7);

        // Extract the authenticated user from the JWT token
        AuthUser authUser = authUserJwtUtils.validateAndExtractToken(token);

        // Verify that said user by ID exists in the database
        if (!userRepository.existsById(authUser.getId())) {
            throw new Exception("Authenticated user does not exist", ErrorCode.INVALID_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
        }

        // Set the authenticated user in the request context
        request.setAttribute("authUser", authUser);

        // Create an Authentication object
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                authUser,
                null,
                authUser.getAuthorities()
        );

        // Set the authentication in the security context holder
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
