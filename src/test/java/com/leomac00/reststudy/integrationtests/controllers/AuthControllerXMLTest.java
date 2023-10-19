package com.leomac00.reststudy.integrationtests.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leomac00.reststudy.configs.TestConfigs;
import com.leomac00.reststudy.integrationtests.testcontainers.AbstractIntegrationTests;
import com.leomac00.reststudy.integrationtests.vo.v1.AccountCredentialsVO;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // This will define an order for the tests to occur based on the setup we're implementing further
class AuthControllerXMLTest extends AbstractIntegrationTests {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;

    @BeforeAll
    private static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // This will disable fails when unknown properties are found on the object, we're doing this because sometimes we might get properties that the VO doens't know about, in this case this is the HATEOAS links
    }

    @Test
    @Order(1)
    void authorizationShouldReturnXMLFormat() throws JsonProcessingException, JsonMappingException {
        AccountCredentialsVO credentials = new AccountCredentialsVO("admin", "admin123");

        var accessTokenXML = given()
                .basePath("/auth")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ACCEPT, TestConfigs.CONTENT_TYPE_XML)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().asString();

        assertNotNull(accessTokenXML);
        assertTrue(accessTokenXML.contains("<username>admin</username>"));
        assertTrue(accessTokenXML.contains("<authenticated>true</authenticated>"));
        assertTrue(accessTokenXML.contains("</created>"));
        assertTrue(accessTokenXML.contains("</expiration>"));
        assertTrue(accessTokenXML.contains("</accessToken>"));
        assertTrue(accessTokenXML.contains("</refreshToken>"));
    }
}
