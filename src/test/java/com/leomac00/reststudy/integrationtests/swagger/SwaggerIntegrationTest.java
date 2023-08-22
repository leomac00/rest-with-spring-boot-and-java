package com.leomac00.reststudy.integrationtests.swagger;

import com.leomac00.reststudy.integrationtests.testcontainers.AbstractIntegrationTests;
import org.springframework.boot.test.context.SpringBootTest;
import com.leomac00.reststudy.configs.TestConfigs;

import static org.junit.jupiter.api.Assertions.*;
import static io.restassured.RestAssured.given;

import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SwaggerIntegrationTest extends AbstractIntegrationTests {

    @Test
    void shouldDIsplaySwaggerUIPage() {
        var content =
                given()
                        .basePath("/swagger-ui/index.html")
                        .port(TestConfigs.SERVER_PORT)
                        .when()
                        .get()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();
        assertTrue(content.contains("Swagger UI"));
    }
}
