package com.playground.api.services;

import com.playground.api.dtos.roles.ListRolesResponse;
import com.playground.api.entities.Role;
import com.playground.api.repositories.RoleRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTests {
    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleService roleService;

    // Response DTOs
    private ListRolesResponse listRolesResponse;

    // Entities
    private Role role;

    // Values used in the tests
    private final UUID uuid = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Set up a mock role
        role = new Role();
        role.setId(uuid);
        role.setName("ADMIN");

        // Set up a mock response DTO
        listRolesResponse = ListRolesResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }

    @Test
    void listRoles_success() {
        // Mock the repository to return a list containing the mock role
        Mockito.when(roleRepository.findAll()).thenReturn(List.of(role));

        // Call the service method
        var response = roleService.listRoles();

        // Verify the response contains the expected role
        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(1, response.size());
        Assertions.assertEquals(listRolesResponse.getId(), response.getFirst().getId());
        Assertions.assertEquals(listRolesResponse.getName(), response.getFirst().getName());
    }
}
