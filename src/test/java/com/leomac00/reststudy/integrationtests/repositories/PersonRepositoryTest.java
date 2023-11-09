package com.leomac00.reststudy.integrationtests.repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.leomac00.reststudy.configs.TestConfigs;
import com.leomac00.reststudy.exceptions.ResourceNotFoundException;
import com.leomac00.reststudy.integrationtests.testcontainers.AbstractIntegrationTests;
import com.leomac00.reststudy.integrationtests.vo.v1.wrapper.WrapperPersonVO;
import com.leomac00.reststudy.models.Person;
import com.leomac00.reststudy.repositories.PersonRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersonRepositoryTest extends AbstractIntegrationTests {
    @Autowired
    public PersonRepository repository;

    private static Person person;

    @BeforeAll
    public static void setup() {
        person = new Person();
    }

    @Test
    @Order(1)
    void testFindByPartialName() throws JsonProcessingException {
        var pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.ASC, "firstName"));
        person = repository.findByPartialFirstName("kirb", pageable).getContent().get(0);

        assertNotNull(person);
        assertEquals("Kirby", person.getFirstName());
    }

    @Test
    @Order(2)
    void testEnable() throws JsonProcessingException {
        var id = 50L;

        repository.enable(id);

        var p = getPersonOrElseThrow(id);

        assertNotNull(p);
        assertEquals(true, p.getEnabled());
    }

    @Test
    @Order(3)
    void testDisable() throws JsonProcessingException {
        var id = 50L;

        repository.disable(id);

        var p = getPersonOrElseThrow(id);

        assertNotNull(p);
        assertEquals(false, p.getEnabled());
    }

    private Person getPersonOrElseThrow(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No person was found for the provided ID!"));
    }
}
