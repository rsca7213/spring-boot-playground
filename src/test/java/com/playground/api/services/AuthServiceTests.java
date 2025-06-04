package com.playground.api.services;

import com.playground.api.dtos.auth.LoginUserBody;
import com.playground.api.dtos.auth.LoginUserResponse;
import com.playground.api.dtos.auth.RegisterUserBody;
import com.playground.api.dtos.auth.RegisterUserResponse;
import com.playground.api.entities.Role;
import com.playground.api.entities.User;
import com.playground.api.repositories.UserRepository;
import com.playground.api.utils.AuthUserJwtUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTests {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthUserJwtUtils authUserJwtUtils;

    @InjectMocks
    private AuthService authService;

    // Request DTOs
    private RegisterUserBody registerUserBody;
    private LoginUserBody loginUserBody;

    // Response DTOs
    private RegisterUserResponse registerUserResponse;
    private LoginUserResponse loginUserResponse;

    // Values used in the tests
    private final String hashedPassword = "hashedPassword123";
    private final UUID uuid = UUID.randomUUID();
    private final String accessToken = "access-token-123";

    @BeforeEach
    void setUp() {
        // Set up request DTOs
        registerUserBody = RegisterUserBody.builder()
                .email("test@springbootplayground.com")
                .firstName("John")
                .lastName("Doe")
                .password("Password123*")
                .build();

        loginUserBody = LoginUserBody.builder()
                .email("test@springbootplayground.com")
                .password("Password123*")
                .build();

        // Set up response DTOs
        registerUserResponse = RegisterUserResponse.builder()
                .id(uuid)
                .build();

        loginUserResponse = LoginUserResponse.builder()
                .token(accessToken)
                .build();

    }

    @Test
    void registerUser_Success() {
        // Mock the response from the user repository to verify that the user does not already exist
        Mockito.when(userRepository.existsByEmailIgnoreCase(registerUserBody.getEmail())).thenReturn(false);

        // Mock the password encoder to return a hashed password
        Mockito.when(passwordEncoder.encode(registerUserBody.getPassword())).thenReturn(hashedPassword);

        // Mock the user repository to return a new user with an ID
        User user = new User();
        user.setId(uuid);
        user.setEmail(registerUserBody.getEmail());
        user.setFirstName(registerUserBody.getFirstName());
        user.setLastName(registerUserBody.getLastName());
        user.setPasswordHash(hashedPassword);
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        // Call the registerUser method
        registerUserResponse = authService.registerUser(registerUserBody);

        // Verify that the user repository was called with the correct email
        Mockito.verify(userRepository).existsByEmailIgnoreCase(registerUserBody.getEmail());

        // Verify that the password encoder was called with the correct password
        Mockito.verify(passwordEncoder).encode(registerUserBody.getPassword());

        // Verify that the user repository was called to save the new user
        Mockito.verify(userRepository).save(Mockito.any(User.class));

        // Assert that the response contains the expected user ID
        Assertions.assertEquals(uuid, registerUserResponse.getId());
    }

    @Test
    void registerUser_EmailAlreadyExists() {
        // Mock the user repository to indicate that a user with the provided email already exists
        Mockito.when(userRepository.existsByEmailIgnoreCase(registerUserBody.getEmail())).thenReturn(true);

        // Call the registerUser method and expect an exception to be thrown
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            authService.registerUser(registerUserBody);
        });

        // Verify that the user repository was called to check for existing email
        Mockito.verify(userRepository).existsByEmailIgnoreCase(registerUserBody.getEmail());

        // Assert that the exception message is as expected
        Assertions.assertEquals("User with this email already exists", exception.getMessage());
    }

    @Test
    void loginUser_Success() {
        // Mock the user repository to return a user with the provided email
        Role role = new Role();
        role.setName("ADMIN");
        User user = new User();
        user.setId(uuid);
        user.setEmail(loginUserBody.getEmail());
        user.setPasswordHash(hashedPassword);
        user.setRole(role);
        Mockito.when(userRepository.findByEmailIgnoreCase(loginUserBody.getEmail())).thenReturn(user);

        // Mock the password encoder to verify the password matches
        Mockito.when(passwordEncoder.matches(loginUserBody.getPassword(), hashedPassword)).thenReturn(true);

        // Mock the JWT utility to return a token
        Mockito.when(authUserJwtUtils.generateToken(Mockito.any())).thenReturn(accessToken);

        // Call the loginUser method
        loginUserResponse = authService.loginUser(loginUserBody);

        // Verify that the user repository was called with the correct email
        Mockito.verify(userRepository).findByEmailIgnoreCase(loginUserBody.getEmail());

        // Verify that the password encoder was called with the correct password and hash
        Mockito.verify(passwordEncoder).matches(loginUserBody.getPassword(), hashedPassword);

        // Verify that the JWT utility was called to generate a token
        Mockito.verify(authUserJwtUtils).generateToken(Mockito.any());

        // Assert that the response contains the expected access token
        Assertions.assertEquals(accessToken, loginUserResponse.getToken());
    }

    @Test
    void loginUser_UserDoesNotExist() {
        // Mock the user repository to return null for the provided email
        Mockito.when(userRepository.findByEmailIgnoreCase(loginUserBody.getEmail())).thenReturn(null);

        // Call the loginUser method and expect an exception to be thrown
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            authService.loginUser(loginUserBody);
        });

        // Verify that the user repository was called with the correct email
        Mockito.verify(userRepository).findByEmailIgnoreCase(loginUserBody.getEmail());

        // Assert that the exception message is as expected
        Assertions.assertEquals("Invalid email or password", exception.getMessage());
    }

    @Test
    void loginUser_InvalidPassword() {
        // Mock the user repository to return a user with the provided email
        User user = new User();
        user.setId(uuid);
        user.setEmail(loginUserBody.getEmail());
        user.setPasswordHash(hashedPassword);
        Mockito.when(userRepository.findByEmailIgnoreCase(loginUserBody.getEmail())).thenReturn(user);

        // Mock the password encoder to indicate that the password does not match
        Mockito.when(passwordEncoder.matches(loginUserBody.getPassword(), hashedPassword)).thenReturn(false);

        // Call the loginUser method and expect an exception to be thrown
        Exception exception = Assertions.assertThrows(Exception.class, () -> {
            authService.loginUser(loginUserBody);
        });

        // Verify that the user repository was called with the correct email
        Mockito.verify(userRepository).findByEmailIgnoreCase(loginUserBody.getEmail());

        // Verify that the password encoder was called with the correct password and hash
        Mockito.verify(passwordEncoder).matches(loginUserBody.getPassword(), hashedPassword);

        // Assert that the exception message is as expected
        Assertions.assertEquals("Invalid email or password", exception.getMessage());
    }
}
