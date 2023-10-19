package com.leomac00.reststudy.integrationtests.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leomac00.reststudy.configs.TestConfigs;
import com.leomac00.reststudy.integrationtests.testcontainers.AbstractIntegrationTests;
import com.leomac00.reststudy.integrationtests.vo.v1.AccountCredentialsVO;
import com.leomac00.reststudy.integrationtests.vo.v1.TokenVO;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // This will define an order for the tests to occur based on the setup we're implementing further
class AuthControllerJSONTest extends AbstractIntegrationTests {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    @BeforeAll
    private static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // This will disable fails when unknown properties are found on the object, we're doing this because sometimes we might get properties that the VO doens't know about, in this case this is the HATEOAS links
    }

    @Test
    @Order(1)
    void authorizationShouldReturnToken() throws JsonProcessingException, JsonMappingException {
        AccountCredentialsVO credentials = new AccountCredentialsVO("admin", "admin123");

        var accessTokenJSON = given()
                .basePath("/auth")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ACCEPT, TestConfigs.CONTENT_TYPE_JSON)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().as(TokenVO.class);

        assertNotNull(accessTokenJSON);
        assertNotNull(accessTokenJSON.getAccessToken());
        assertNotNull(accessTokenJSON.getRefreshToken());
    }

    @Test
    @Order(2)
    void refreshTokenGeneration() throws JsonProcessingException, JsonMappingException {
        AccountCredentialsVO credentials = new AccountCredentialsVO("admin", "admin123");

        var accessTokenJSON = given()
                .basePath("/auth")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ACCEPT, TestConfigs.CONTENT_TYPE_JSON)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().as(TokenVO.class);

        var refreshTokenJSON = given()
                .basePath("/auth/refresh/" + credentials.getUsername())
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ACCEPT, TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer " + accessTokenJSON.getRefreshToken())
                .when()
                .put()
                .then()
                .statusCode(200)
                .extract()
                .body().as(TokenVO.class);

        assertNotNull(refreshTokenJSON);
        assertNotNull(refreshTokenJSON.getAccessToken());
        assertNotNull(refreshTokenJSON.getRefreshToken());
    }
}