package com.leomac00.reststudy.integrationtests.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.leomac00.reststudy.configs.TestConfigs;
import com.leomac00.reststudy.integrationtests.testcontainers.AbstractIntegrationTests;
import com.leomac00.reststudy.integrationtests.vo.v1.AccountCredentialsVO;
import com.leomac00.reststudy.integrationtests.vo.v1.TokenVO;
import com.leomac00.reststudy.integrationtests.vo.v1.PersonVO;
import com.leomac00.reststudy.integrationtests.vo.v1.wrapper.WrapperPersonVO;
import com.leomac00.reststudy.mocks.MockPersonEnumValues;
import com.leomac00.reststudy.models.Person;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
    @Order(0)
    void authorization() throws JsonProcessingException, JsonMappingException {
        AccountCredentialsVO credentials = new AccountCredentialsVO("admin", "admin123");

        var accessToken = given()
                .basePath("/auth")
                .port(TestConfigs.SERVER_PORT)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .body(credentials)
                .when()
                .post()
                .then()
                .statusCode(200)
                .extract()
                .body().as(TokenVO.class)
                .getAccessToken();

        specification = new RequestSpecBuilder()
                .addHeader(TestConfigs.HEADER_PARAM_AUTHORIZATION, "Bearer ".concat(accessToken)) // now this will set the specification for the rest of the tests here
                .setBasePath("/api/person/v1")
                .setPort(TestConfigs.SERVER_PORT)
                .addFilter(new RequestLoggingFilter(LogDetail.ALL))
                .addFilter(new ResponseLoggingFilter(LogDetail.ALL))
                .build();
    }

    @Test
    @Order(1)
    void testCreate() throws JsonProcessingException {
        mockPerson();

        var content =
                given().spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
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
        assertEquals(person.getEnabled(), true);
    }

    @Test
    @Order(2)
    void testCreateWithInvalidOrigin() throws JsonProcessingException {
        mockPerson();

        var content =
                given().spec(specification)
                        .contentType(TestConfigs.CONTENT_TYPE_JSON)
                        .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.INVALID_ORIGIN)
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

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", person.getId())
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
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
        assertEquals(person.getEnabled(), true);
    }

    @Test
    @Order(4)
    void testFindAll() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
                .when()
                .get("findAll")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        var wrapperPersonVO = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapperPersonVO.getEmbeddedVO().getPersonVOList();

        var singlePerson = people.get(0);

        assertNotNull(singlePerson.getId());
        assertNotNull(singlePerson.getAddress());
        assertNotNull(singlePerson.getFirst_name());
        assertNotNull(singlePerson.getLast_name());
        assertNotNull(singlePerson.getGender());
        assertNotNull(singlePerson.getEnabled());
    }

    @Test
    @Order(5)
    void testFindByIdWithInvalidOrigin() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.INVALID_ORIGIN)
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

    @Test
    @Order(6)
    void testDisable() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", person.getId())
                .when()
                .patch("disable/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var responsePerson = objectMapper.readValue(content, PersonVO.class);

        assertTrue(responsePerson.getId() > 0);
        assertNotNull(responsePerson);
        assertEquals(person.getAddress(), responsePerson.getAddress());
        assertEquals(person.getFirst_name(), responsePerson.getFirst_name());
        assertEquals(person.getLast_name(), responsePerson.getLast_name());
        assertEquals(person.getGender(), responsePerson.getGender());
        assertEquals(false, responsePerson.getEnabled());
    }

    @Test
    @Order(7)
    void testEnable() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .pathParam("id", person.getId())
                .when()
                .patch("enable/{id}")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();

        var responsePerson = objectMapper.readValue(content, PersonVO.class);

        assertTrue(responsePerson.getId() > 0);
        assertNotNull(responsePerson);
        assertEquals(person.getAddress(), responsePerson.getAddress());
        assertEquals(person.getFirst_name(), responsePerson.getFirst_name());
        assertEquals(person.getLast_name(), responsePerson.getLast_name());
        assertEquals(person.getGender(), responsePerson.getGender());
        assertEquals(true, responsePerson.getEnabled());
    }

    @Test
    @Order(8)
    void testAscSortingDirection() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
                .queryParam(TestConfigs.DIRECTION, TestConfigs.ASCENDING)
                .when()
                .get("findAll")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        var wrapperPersonVO = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapperPersonVO.getEmbeddedVO().getPersonVOList();

        var priorPerson = people.get(0);
        var postPerson = people.get(1);

        assertTrue(priorPerson.getId() < postPerson.getId());
    }

    @Test
    @Order(9)
    void testDescSortingDirection() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
                .queryParam(TestConfigs.DIRECTION, TestConfigs.DESCENDING)
                .when()
                .get("findAll")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        var wrapperPersonVO = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapperPersonVO.getEmbeddedVO().getPersonVOList();

        var priorPerson = people.get(0);
        var postPerson = people.get(1);

        assertTrue(priorPerson.getId() > postPerson.getId());
    }

    @Test
    @Order(10)
    void testPageSize() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
                .queryParam(TestConfigs.PAGE_LIMIT, 10)
                .queryParam(TestConfigs.PAGE_NUMBER, 3)
                .when()
                .get("findAll")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        var wrapperPersonVO = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapperPersonVO.getEmbeddedVO().getPersonVOList();

        assertEquals(10, people.stream().count());
        assertTrue(20 < people.get(5).getId());
    }

    @Test
    @Order(11)
    void testIsInPage10() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
                .queryParam(TestConfigs.PAGE_NUMBER, 10)
                .when()
                .get("findAll")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        var wrapperPersonVO = objectMapper.readValue(content, WrapperPersonVO.class);

        assertEquals(10, wrapperPersonVO.getPage().getNumber());
    }

    @Test
    @Order(12)
    void testFindByPartialName() throws JsonProcessingException {
        mockPerson();

        var content = given().spec(specification)
                .contentType(TestConfigs.CONTENT_TYPE_JSON)
                .header(TestConfigs.HEADER_PARAM_ORIGIN, TestConfigs.VALID_ORIGIN)
                .queryParam(TestConfigs.FIND_BY_PARTIAL_NAME, "kirby")
                .when()
                .get("findByPartialName")
                .then()
                .statusCode(200)
                .extract()
                .body()
                .asString();
        var wrapperPersonVO = objectMapper.readValue(content, WrapperPersonVO.class);
        var people = wrapperPersonVO.getEmbeddedVO().getPersonVOList();
        var person = people.get(0);
        assertNotNull(person);
        assertEquals("Kirby", person.getFirst_name());
    }

    private void mockPerson() {
        person.setAddress(MockPersonEnumValues.ADDRESS.value);
        person.setGender(MockPersonEnumValues.GENDER_FEMALE.value);
        person.setFirst_name(MockPersonEnumValues.FIRST_NAME.value);
        person.setLast_name(MockPersonEnumValues.LAST_NAME.value);
        person.setEnabled(true);
    }
}
