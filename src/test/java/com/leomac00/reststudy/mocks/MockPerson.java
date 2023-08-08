package com.leomac00.reststudy.mocks;

import com.leomac00.reststudy.data.vo.v1.PersonVO;
import com.leomac00.reststudy.models.Person;

import java.util.ArrayList;
import java.util.List;

public class MockPerson {

    public Person mockEntity() {
        return mockEntity(0);
    }

    public PersonVO mockVO() {
        return mockVO(0);
    }

    public List<Person> mockEntityList() {
        List<Person> persons = new ArrayList<Person>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonVO> mockVOList() {
        List<PersonVO> persons = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockVO(i));
        }
        return persons;
    }

    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setAddress(MockPersonEnumValues.ADDRESS.value + number);
        person.setFirstName(MockPersonEnumValues.FIRST_NAME.value + number);
        person.setGender(((number % 2) == 0) ? MockPersonEnumValues.GENDER_MALE.value : MockPersonEnumValues.GENDER_FEMALE.value);
        person.setId(number.longValue());
        person.setLastName(MockPersonEnumValues.LAST_NAME.value + number);
        return person;
    }

    public PersonVO mockVO(Integer number) {
        PersonVO person = new PersonVO();
        person.setAddress(MockPersonEnumValues.ADDRESS.value + number);
        person.setFirstName(MockPersonEnumValues.FIRST_NAME.value + number);
        person.setGender(((number % 2) == 0) ? MockPersonEnumValues.GENDER_MALE.value : MockPersonEnumValues.GENDER_FEMALE.value);
        person.setKey(number.longValue());
        person.setLastName(MockPersonEnumValues.LAST_NAME.value + number);
        return person;
    }

}
