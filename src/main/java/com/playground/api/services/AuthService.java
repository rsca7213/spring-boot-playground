package com.playground.api.services;

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

    @Autowired
    public AuthService(
            UserRepository userRepository,
            AuthUserJwtUtils authUserJwtUtils,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        return new RegisterUserResponse(user.getId());
    }
}
