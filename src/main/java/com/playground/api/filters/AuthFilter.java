package com.playground.api.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.ApiException;
import com.playground.api.exceptions.ErrorResponse;
import com.playground.api.models.AuthUser;
import com.playground.api.repositories.UserRepository;
import com.playground.api.utils.AuthUserJwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class AuthFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;
    private final AuthUserJwtUtils authUserJwtUtils;
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> EXCLUDED_PATHS = List.of(
            "/auth/login",
            "/actuator/health",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/docs/**"
    );

    @Autowired
    public AuthFilter(UserRepository userRepository, AuthUserJwtUtils authUserJwtUtils) {
        this.userRepository = userRepository;
        this.authUserJwtUtils = authUserJwtUtils;
    }

    @Override
    protected boolean shouldNotFilter(@NonNull HttpServletRequest request) {
        String path = request.getRequestURI();
        return EXCLUDED_PATHS.stream().anyMatch(pattern -> pathMatcher.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        // Extract the JWT auth token from a server HTTP-Only cookie named "auth"
        String token = null;
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            Optional<String> value = Arrays.stream(cookies)
                    .filter(cookie -> "auth".equals(cookie.getName()))
                    .map(Cookie::getValue)
                    .findFirst();

            if (value.isPresent()) {
                token = value.get();
            }
        }


        // If the token is not present, continue the filter chain
        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extract the authenticated user from the JWT token
            AuthUser authUser = authUserJwtUtils.validateAndExtractToken(token);

            // Verify that said user by ID exists in the database
            if (!userRepository.existsById(authUser.getId())) {
                throw new ApiException("Authenticated user does not exist", ErrorCode.INVALID_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
            }

            // Set the authenticated user in the request context
            request.setAttribute("authUser", authUser);

            // Create an Authentication object
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authUser, null, authUser.getAuthorities());

            // Set the authentication in the security context holder
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (ApiException e) {
            // Clean the authentication cookie
            Cookie authCookie = new Cookie("auth", "");
            authCookie.setHttpOnly(true);
            authCookie.setPath("/");
            authCookie.setMaxAge(0);
            response.addCookie(authCookie);

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            ErrorResponse errorResponse = ErrorResponse.builder()
                    .status(HttpStatus.UNAUTHORIZED.value())
                    .statusText(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                    .message(e.getMessage())
                    .errorCode(ErrorCode.INVALID_AUTHENTICATION)
                    .build();
            response.setContentType("application/json");
            OutputStream out = response.getOutputStream();
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, errorResponse);
            out.flush();
            return;
        }


        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
