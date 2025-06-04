package com.playground.api.services;

import com.playground.api.dtos.auth.LoginUserBody;
import com.playground.api.dtos.auth.LoginUserResponse;
import com.playground.api.dtos.auth.RegisterUserBody;
import com.playground.api.dtos.auth.RegisterUserResponse;
import com.playground.api.entities.User;
import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.Exception;
import com.playground.api.models.AuthUser;
import com.playground.api.repositories.UserRepository;
import com.playground.api.utils.AuthUserJwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthUserJwtUtils authUserJwtUtils;

    @Autowired
    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthUserJwtUtils authUserJwtUtils
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authUserJwtUtils = authUserJwtUtils;
    }

    public RegisterUserResponse registerUser(RegisterUserBody registerUserBody) {
        // Check if the user already exists with the provided email
        if (userRepository.existsByEmailIgnoreCase(registerUserBody.getEmail())) {
            throw new Exception("User with this email already exists", ErrorCode.ITEM_ALREADY_EXISTS, HttpStatus.CONFLICT);
        }

        // Hash the password before saving
        String hashedPassword = passwordEncoder.encode(registerUserBody.getPassword());

        // Create a new User with the provided details
        User user = new User();
        user.setEmail(registerUserBody.getEmail());
        user.setFirstName(registerUserBody.getFirstName());
        user.setLastName(registerUserBody.getLastName());
        user.setPasswordHash(hashedPassword);

        // Save the user to the database
        user = userRepository.save(user);

        // Return the response with the newly created user's ID
        return RegisterUserResponse.builder()
                .id(user.getId())
                .build();
    }

    public LoginUserResponse loginUser(LoginUserBody loginUserBody) {
        // Find the user by email
        User user = userRepository.findByEmailIgnoreCase(loginUserBody.getEmail());

        // If the user does not exist, throw an exception
        if (user == null) {
            throw new Exception("Invalid email or password", ErrorCode.INVALID_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
        }

        // Verify the password
        if (!passwordEncoder.matches(loginUserBody.getPassword(), user.getPasswordHash())) {
            throw new Exception("Invalid email or password", ErrorCode.INVALID_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
        }

        // Generate a JWT token for the authenticated user
        AuthUser authUser = AuthUser.fromUser(user);
        String token = authUserJwtUtils.generateToken(authUser);

        // Return the response with the token and user details
        return LoginUserResponse.builder()
                .token(token)
                .build();

    }
}
