package com.leomac00.reststudy.unittests;

import com.leomac00.reststudy.data.vo.v1.PersonVO;
import com.leomac00.reststudy.mocks.MockPerson;
import com.leomac00.reststudy.mocks.MockPersonEnumValues;
import com.leomac00.reststudy.models.Person;
import com.leomac00.reststudy.repositories.PersonRepository;
import com.leomac00.reststudy.services.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    MockPerson input;
    private int id = 1;
    private String ad = MockPersonEnumValues.ADDRESS.value + id;
    private String fn = MockPersonEnumValues.FIRST_NAME.value + id;
    private String ln = MockPersonEnumValues.LAST_NAME.value + id;
    private String gm = MockPersonEnumValues.GENDER_MALE.value;
    private String gf = MockPersonEnumValues.GENDER_FEMALE.value;

    private ModelMapper mapper = new ModelMapper(); //Note here I created an instance of ModelMapper
    @Mock
    private PersonRepository repository;
    @InjectMocks
    private PersonService service = new PersonService(mapper); //Here when creating the PersonService I passed the ModelMapper dependency that it has


    @BeforeEach
    void setUp() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);
        //And here I had to set the typeMap value that I set in Person Service
        PersonService.PersonServiceMappingConfig(mapper);
    }

    @Test
    void findById() {

        Person person = input.mockEntity(id);

        when(repository.findById((long) id)).thenReturn(Optional.ofNullable(person)); // When repository tries to get repository.findById the mock will return our "person" passed in the reutnr block

        var result = service.findById((long) id); //Grab person in our mocked service which will access the mocked repository

        assertNotNull(result);
        assertEquals((long) result.getKey(), (long) id);
        assertEquals(result.getAddress(), ad);
        assertEquals(result.getGender(), gf);
        assertEquals(result.getFirstName(), fn);
        assertEquals(result.getLastName(), ln);
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().toString().contains("</api/person/v1/1>;rel=\"self\""));
    }

    @Test
    void findAll() {
    }

    @Test
    void create() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}