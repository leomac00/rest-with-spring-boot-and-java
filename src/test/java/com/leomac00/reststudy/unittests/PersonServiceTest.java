package com.leomac00.reststudy.unittests;

import com.leomac00.reststudy.config.CustomModelMapper;
import com.leomac00.reststudy.data.vo.v1.PersonVO;
import com.leomac00.reststudy.exceptions.RequiredObjectIsNullException;
import com.leomac00.reststudy.mocks.MockPerson;
import com.leomac00.reststudy.mocks.MockPersonEnumValues;
import com.leomac00.reststudy.models.Person;
import com.leomac00.reststudy.repositories.PersonRepository;
import com.leomac00.reststudy.services.PersonService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    MockPerson input;
    private int id = 1;
    private String ad = MockPersonEnumValues.ADDRESS.getMockValue(id);
    private String fn = MockPersonEnumValues.FIRST_NAME.getMockValue(id);
    private String ln = MockPersonEnumValues.LAST_NAME.getMockValue(id);
    private String gm = MockPersonEnumValues.GENDER_MALE.value;
    private String gf = MockPersonEnumValues.GENDER_FEMALE.value;

    @Mock
    private ModelMapper mapper; //Note here I created an instance of ModelMapper
    @Mock
    private PersonRepository repository;

    private ModelMapper cmm = new CustomModelMapper().customMapper();
    @InjectMocks
    private PersonService service; //Here when creating the PersonService I passed the ModelMapper dependency that it has


    @BeforeEach
    void setUpBE() {
        input = new MockPerson();
        MockitoAnnotations.openMocks(this);

    }

    @Test
    void findById() {
        Person entity = input.mockEntity(id);
        when(repository.findById((long) id)).thenReturn(Optional.ofNullable(entity)); // When repository tries to get repository.findById the mock will return our "person" passed in the reutnr block
        when(mapper.map(entity, PersonVO.class)).thenReturn(cmm.map(entity, PersonVO.class));
        var result = service.findById((long) id); //Grab person in our mocked service which will access the mocked repository

        assertHappyPathResultValues(result);
    }

    @Test
    void create() {
        Person entity = input.mockEntity(id);

        Person persisted = entity;

        PersonVO vo = input.mockVO(id);

        when(repository.save(entity)).thenReturn(persisted);
        when(mapper.map(vo, Person.class)).thenReturn(cmm.map(vo, Person.class));
        when(mapper.map(entity, PersonVO.class)).thenReturn(cmm.map(entity, PersonVO.class));

        var result = service.create(vo);

        assertHappyPathResultValues(result);
    }

    @Test
    void createWithNullPerson() {
        Exception ex = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.create(null);
        });

        assertEquals(ex.getMessage(), "Request body is required!");
    }

    @Test
    void update() {
        Person entity = input.mockEntity(id);

        Person persisted = entity;

        PersonVO vo = input.mockVO(id);

        // We have to use the 2 "when's" here because there are the two methods that the update operation is using inside
        when(repository.save(entity)).thenReturn(persisted);
        when(repository.findById((long) id)).thenReturn(Optional.ofNullable(entity));
        when(mapper.map(entity, PersonVO.class)).thenReturn(cmm.map(entity, PersonVO.class));

        var result = service.update(vo);

        assertHappyPathResultValues(result);
    }

    @Test
    void updateWithNullPerson() {
        Exception ex = assertThrows(RequiredObjectIsNullException.class, () -> {
            service.update(null);
        });

        assertEquals(ex.getMessage(), "Request body is required!");
    }

    private void assertHappyPathResultValues(PersonVO result) {
        assertNotNull(result);
        assertEquals((long) result.getKey(), id);
        assertEquals(result.getAddress(), ad);
        assertEquals(result.getGender(), gf);
        assertEquals(result.getFirstName(), fn);
        assertEquals(result.getLastName(), ln);
        assertNotNull(result.getLinks());
        assertTrue(result.getLinks().toString().contains("</api/person/v1/1>;rel=\"self\""));
    }
}