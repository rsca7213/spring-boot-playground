package com.playground.api.models;

import com.playground.api.entities.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthUser extends JwtPayload implements UserDetails {
    UUID id;
    String email;
    String firstName;
    String lastName;

    public static AuthUser fromUser(User user) {
        AuthUser authUser = new AuthUser();
        authUser.setId(user.getId());
        authUser.setEmail(user.getEmail());
        authUser.setFirstName(user.getFirstName());
        authUser.setLastName(user.getLastName());
        return authUser;
    }


    @Override
    public Map<String, Object> toMap() {
        return Map.of(
                "id", id.toString(),
                "email", email,
                "firstName", firstName,
                "lastName", lastName
        );
    }

    @Override
    public void fromMap(Map<String, Object> map) {
        this.id = UUID.fromString((String) map.get("id"));
        this.email = (String) map.get("email");
        this.firstName = (String) map.get("firstName");
        this.lastName = (String) map.get("lastName");
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public String getPassword() {
        return null; // Password is not stored in AuthUser, it's managed by User entity
    }

    @Override
    public String getUsername() {
        return email; // Use email as username
    }
}
