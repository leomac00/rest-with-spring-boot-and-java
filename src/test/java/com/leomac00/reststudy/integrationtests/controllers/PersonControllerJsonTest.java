package com.leomac00.reststudy.integrationtests.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leomac00.reststudy.configs.TestConfigs;
import com.leomac00.reststudy.integrationtests.testcontainers.AbstractIntegrationTests;
import com.leomac00.reststudy.integrationtests.vo.v1.PersonVO;
import com.leomac00.reststudy.mocks.MockPersonEnumValues;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // This will define an order for the tests to occur based on the setup we're implementing further
class PersonControllerJsonTest extends AbstractIntegrationTests {

    private static RequestSpecification specification;
    private static ObjectMapper objectMapper;
    private static PersonVO person;

    @BeforeAll
    private static void setup() {
        objectMapper = new ObjectMapper();
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // This will disable fails when unknown properties are found on the object, we're doing this because sometimes we might get properties that the VO doens't know about, in this case this is the HATEOAS links
        person = new PersonVO();
    }

    @Test
    @Order(1)
    void testCreate() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build(); // We need ot set the specification here because here inside the test is where the spring context is running, thus we have to set stuff related to this in here
        var content =
                given().spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(person)
                        .when()
                        .post()
                        .then()
                        .statusCode(200)
                        .extract()
                        .body()
                        .asString();
        var createdPerson = objectMapper.readValue(content, PersonVO.class);
        person = createdPerson;

        assertTrue(createdPerson.getId() > 0);
        assertNotNull(createdPerson);
        assertEquals(person.getAddress(), createdPerson.getAddress());
        assertEquals(person.getFirst_name(), createdPerson.getFirst_name());
        assertEquals(person.getLast_name(), createdPerson.getLast_name());
        assertEquals(person.getGender(), createdPerson.getGender());
    }

    @Test
    @Order(2)
    void testCreateWithInvalidOrigin() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.INVALID_ORIGIN)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build(); // We need ot set the specification here because here inside the test is where the spring context is running, thus we have to set stuff related to this in here
        var content =
                given().spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .body(person)
                        .when()
                        .post()
                        .then()
                        .statusCode(403)
                        .extract()
                        .body()
                        .asString();
        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }

    @Test
    @Order(3)
    void testFindById() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var foundPerson = objectMapper.readValue(content, PersonVO.class);

        assertTrue(foundPerson.getId() > 0);
        assertNotNull(foundPerson);
        assertEquals(person.getAddress(), foundPerson.getAddress());
        assertEquals(person.getFirst_name(), foundPerson.getFirst_name());
        assertEquals(person.getLast_name(), foundPerson.getLast_name());
        assertEquals(person.getGender(), foundPerson.getGender());
    }

    @Test
    @Order(4)
    void testFindByIdWithInvalidOrigin() throws JsonProcessingException {
        mockPerson();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.INVALID_ORIGIN)
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", person.getId())
                .when()
                .get("{id}")
                .then()
                .statusCode(403)
                .extract()
                .body()
                .asString();

        assertNotNull(content);
        assertEquals("Invalid CORS request", content);
    }


    private void mockPerson() {
        person.setAddress(MockPersonEnumValues.ADDRESS.value);
        person.setGender(MockPersonEnumValues.GENDER_FEMALE.value);
        person.setFirst_name(MockPersonEnumValues.FIRST_NAME.value);
        person.setLast_name(MockPersonEnumValues.LAST_NAME.value);
    }
}
