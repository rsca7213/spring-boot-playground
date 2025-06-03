package com.playground.api.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuthUser extends JwtPayload {
    UUID id;
    String email;
    String firstName;
    String lastName;


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
}
