package com.playground.api.services;

import com.playground.api.dtos.auth.*;
import com.playground.api.entities.Role;
import com.playground.api.entities.User;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.ApiException;
import com.playground.api.models.AuthUser;
import com.playground.api.repositories.RoleRepository;
import com.playground.api.repositories.UserRepository;
import com.playground.api.utils.AuthUserJwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserJwtUtils authUserJwtUtils;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder,
            AuthUserJwtUtils authUserJwtUtils
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authUserJwtUtils = authUserJwtUtils;
    }

    public RegisterUserResponse registerUser(RegisterUserBody registerUserBody) {
        // Validate that the role exists
        Role role = roleRepository.findById(registerUserBody.getRoleId())
                .orElseThrow(() -> new ApiException("Role does not exist", ErrorCode.ITEM_DOES_NOT_EXIST, HttpStatus.BAD_REQUEST));

        // Check if the user already exists with the provided email
        if (userRepository.existsByEmailIgnoreCase(registerUserBody.getEmail())) {
            throw new ApiException("User with this email already exists", ErrorCode.ITEM_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(registerUserBody.getPassword());

        // Create a new User with the provided details
        User user = new User();
        user.setEmail(registerUserBody.getEmail());
        user.setFirstName(registerUserBody.getFirstName());
        user.setLastName(registerUserBody.getLastName());
        user.setPasswordHash(hashedPassword);
        user.setRole(role);

        // Save the user to the database
        user = userRepository.save(user);

        // Return the response with the newly created user's ID
        return RegisterUserResponse.builder()
                .id(user.getId())
                .build();
    }

    public String loginUser(LoginUserBody loginUserBody) {
        // Find the user by email
        User user = userRepository.findByEmailIgnoreCase(loginUserBody.getEmail());

        // If the user does not exist, throw an exception
        if (user == null) {
            throw new ApiException("Invalid email or password", ErrorCode.INVALID_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
        }

        // Verify the password
        if (!passwordEncoder.matches(loginUserBody.getPassword(), user.getPasswordHash())) {
            throw new ApiException("Invalid email or password", ErrorCode.INVALID_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
        }

        // Generate a JWT token for the authenticated user
        AuthUser authUser = AuthUser.fromUser(user);

        // Return the response token;
        return authUserJwtUtils.generateToken(authUser);

    }

    public GetCurrentUserResponse getCurrentUser(Authentication authentication) {
        // Check if the user is authenticated
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ApiException("User is not authenticated", ErrorCode.INVALID_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
        }

        // Get the authenticated user from the security context
        AuthUser authUser = (AuthUser) authentication.getPrincipal();

        // Return the response with the authenticated user's details
        return GetCurrentUserResponse.builder()
                .id(authUser.getId())
                .email(authUser.getEmail())
                .firstName(authUser.getFirstName())
                .lastName(authUser.getLastName())
                .roleName(authUser.getRoleName())
                .build();
    }
}
