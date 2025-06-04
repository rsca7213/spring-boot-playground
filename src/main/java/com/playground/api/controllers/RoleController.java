package com.playground.api.controllers;

import com.playground.api.dtos.roles.ListRolesResponse;
import com.playground.api.services.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roles")
@Tag(name = "Roles", description = "Endpoints for managing roles")
public class RoleController {
    private final RoleService roleService;

    @Autowired
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(
            summary = "List all roles",
            description = "Retrieves a list of all available roles in the system."
    )
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ListRolesResponse>> listRoles() {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.listRoles());
    }
}
