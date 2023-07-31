package com.leomac00.reststudy.services;

import com.leomac00.reststudy.exceptions.ResourceNotFoundException;
import com.leomac00.reststudy.models.Person;
import com.leomac00.reststudy.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;
    private Logger logger = Logger.getLogger(PersonService.class.getName());
    private final String notFoundMessage = "No person was found for the provided ID!";

    public Person findById(Long id) {
        logger.info("Finding one person");

        return getPersonOrElseThrow(id);
    }

    public List<Person> findAll() {
        logger.info("Finding all persons");

        return personRepository.findAll();
    }

    public Person create(Person person) {
        logger.info("Creating person");

        return personRepository.save(person);
    }

    public Person update(Person newPersonData) {
        logger.info("Updating person");

        var entity = getPersonOrElseThrow(newPersonData.getId());

        entity.setFirstName(newPersonData.getFirstName());
        entity.setLastName(newPersonData.getLastName());
        entity.setAddress(newPersonData.getAddress());
        entity.setGender(newPersonData.getGender());

        return personRepository.save(entity);
    }

    public void delete(Long id) {
        logger.info("Deleting person");
        var entity = getPersonOrElseThrow(id);
        personRepository.delete(entity);
    }

    private Person getPersonOrElseThrow(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }
}
