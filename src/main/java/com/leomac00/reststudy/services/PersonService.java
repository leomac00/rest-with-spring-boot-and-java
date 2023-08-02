package com.leomac00.reststudy.services;

import com.leomac00.reststudy.data.vo.v1.PersonVO;
import com.leomac00.reststudy.exceptions.ResourceNotFoundException;
import com.leomac00.reststudy.models.Person;
import com.leomac00.reststudy.repositories.PersonRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    ModelMapper mapper;
    private Logger logger = Logger.getLogger(PersonService.class.getName());
    private final String notFoundMessage = "No person was found for the provided ID!";

    public PersonVO findById(Long id) {
        logger.info("Finding one person");
        var person = getPersonOrElseThrow(id);
        return mapper.map(person, PersonVO.class);
    }

    public List<PersonVO> findAll() {
        logger.info("Finding all persons");

        List<Person> entities = personRepository.findAll();
        List<PersonVO> vos = new ArrayList<>();

        entities.forEach(entity -> {
            vos.add(mapper.map(entity, PersonVO.class));
        });
        return vos;
    }

    public PersonVO create(PersonVO personVO) {
        logger.info("Creating person");
        
        var entity = mapper.map(personVO, Person.class);
        var vo = mapper.map(personRepository.save(entity), PersonVO.class);
        return vo;
    }

    public PersonVO update(PersonVO newPersonData) {
        logger.info("Updating person");

        var entity = getPersonOrElseThrow(newPersonData.getId());

        entity.setFirstName(newPersonData.getFirstName());
        entity.setLastName(newPersonData.getLastName());
        entity.setAddress(newPersonData.getAddress());
        entity.setGender(newPersonData.getGender());

        var vo = mapper.map(personRepository.save(entity), PersonVO.class);
        return vo;
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

    public PersonVO createv2(PersonVO personVO) {
        logger.info("Creating person");

        var entity = mapper.map(personVO, Person.class);
        entity.setAddress(entity.getAddress() + "v2");
        var vo = mapper.map(personRepository.save(entity), PersonVO.class);
        return vo;
    }
}
