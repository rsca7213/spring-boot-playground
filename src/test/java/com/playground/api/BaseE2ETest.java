package com.playground.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class BaseE2ETest {
    // Authorization variables
    protected static String adminAccessToken;
    protected static String clientAccessToken;
    protected static final String AUTH_COOKIE_NAME = "auth";
    protected static final String ADMIN_EMAIL = "admin@springbootplayground.com";
    protected static final String CLIENT_EMAIL = "client@springbootplayground.com";
    protected static final String TEST_PASSWORD = "Password123*";

    @LocalServerPort
    protected Integer port;

    @Container
    @ServiceConnection
    static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");


    @BeforeEach
    void setupBase() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
        RestAssured.basePath = "/api";
    }

    private String obtainAuthCookie(String email, String password) {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .body("{\"email\": \"" + email + "\", \"password\": \"" + password + "\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .cookie(AUTH_COOKIE_NAME);
    }

    protected String obtainAdminAccessToken() {
        if (adminAccessToken == null) {
            adminAccessToken = obtainAuthCookie(ADMIN_EMAIL, TEST_PASSWORD);
        }

        return adminAccessToken;
    }

    protected String obtainClientAccessToken() {
        if (clientAccessToken == null) {
            clientAccessToken = obtainAuthCookie(CLIENT_EMAIL, TEST_PASSWORD);
        }

        return clientAccessToken;
    }

    protected RequestSpecification requestWithAdminAuth() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie(AUTH_COOKIE_NAME, obtainAdminAccessToken());
    }

    protected RequestSpecification requestWithClientAuth() {
        return RestAssured.given()
                .contentType(ContentType.JSON)
                .cookie(AUTH_COOKIE_NAME, obtainClientAccessToken());
    }
}
