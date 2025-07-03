package com.playground.api.controllers;

import com.playground.api.BaseE2ETest;
import io.restassured.http.ContentType;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class RoleControllerTests extends BaseE2ETest {
    @Test
    @Transactional
    void listRoles_success() {
        requestWithAdminAuth()
                .when()
                .get("/roles")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0))
                .body("[0].name", equalTo("ADMIN"))
                .body("[1].name", equalTo("CLIENT"));
    }
}
