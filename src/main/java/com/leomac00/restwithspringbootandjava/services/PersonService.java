package com.leomac00.restwithspringbootandjava.services;

import com.leomac00.restwithspringbootandjava.models.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

@Service
public class PersonService {
    private final AtomicLong counter = new AtomicLong();
    private Logger logger = Logger.getLogger(PersonService.class.getName());

    public Person findById(String id){
        logger.info("Finding one person");
        Person p = new Person(counter.getAndIncrement(), "fname","lname","address");
        return p;
    }
    public List<Person> findAll(){
        logger.info("Finding all persons");
        List<Person> persons = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Person p = mockPerson(i);
            persons.add(p);
        }
        return persons;
    }
    public Person create(Person person){
        logger.info("Creating person");

        return person;
    }
    public Person update(Person person){
        logger.info("Updating person");

        return person;
    }
    public void delete(String id){
        logger.info("Deleting person");
    }


    private Person mockPerson(int i) {
        return new Person(counter.getAndIncrement(),
                "fname".concat(String.valueOf(i)),
                "lname".concat(String.valueOf(i)),
                "address".concat(String.valueOf(i)));
    }
}
