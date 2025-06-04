package com.playground.api.services;

import com.playground.api.dtos.roles.ListRolesResponse;
import com.playground.api.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<ListRolesResponse> listRoles() {
        return roleRepository.findAll()
                .stream()
                .map(role -> ListRolesResponse.builder()
                        .id(role.getId())
                        .name(role.getName())
                        .build())
                .toList();
    }
}
