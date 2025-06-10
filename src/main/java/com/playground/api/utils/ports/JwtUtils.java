package com.playground.api.utils.ports;

import com.playground.api.enums.ErrorCode;
import com.playground.api.exceptions.Exception;
import com.playground.api.models.JwtPayload;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

public abstract class JwtUtils<T extends JwtPayload> {
    @Value("${jwt.secret}")
    protected String jwtSecret;

    @Value("${jwt.expiration}")
    protected long jwtExpiration;

    @Value("${jwt.issuer}")
    protected String jwtIssuer;

    protected SecretKey jwtKey;

    @PostConstruct
    public void init() {
        jwtKey = Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(T payload) {
        String subject = payload.getId() != null ? payload.getId().toString() : null;
        String jti = UUID.randomUUID().toString();

        return Jwts.builder()
                .expiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000))
                .issuedAt(new Date())
                .issuer(jwtIssuer)
                .id(jti)
                .subject(subject)
                .claims(payload.toMap())
                .signWith(jwtKey)
                .compact();
    }

    public T validateAndExtractToken(String token) {
        try {
             Jwt<?, ?> jwt = Jwts.parser()
                    .verifyWith(jwtKey)
                    .build()
                    .parse(token);

            Claims claims = (Claims) jwt.getPayload();

            T payload = createPayloadInstance(claims);

            if (payload.getId() == null) {
                throw new Exception("Invalid token", ErrorCode.INVALID_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
            }
            return payload;

        } catch (SecurityException | MalformedJwtException | IllegalArgumentException e) {
            throw new Exception("Invalid token", ErrorCode.INVALID_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            throw new Exception("Token has expired", ErrorCode.INVALID_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            throw new Exception("Unsupported token", ErrorCode.INVALID_AUTHENTICATION, HttpStatus.UNAUTHORIZED);
        }
    }

    protected abstract T createPayloadInstance(Claims claims);
}