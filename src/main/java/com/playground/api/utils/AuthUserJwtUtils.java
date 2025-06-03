package com.playground.api.utils;

import com.playground.api.models.AuthUser;
import com.playground.api.utils.ports.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;

@Service
public class AuthUserJwtUtils extends JwtUtils<AuthUser> {
    @Override
    protected AuthUser createPayloadInstance(Claims claims) {
        AuthUser authUser = new AuthUser();
        authUser.fromMap(claims);
        return authUser;
    }
}
