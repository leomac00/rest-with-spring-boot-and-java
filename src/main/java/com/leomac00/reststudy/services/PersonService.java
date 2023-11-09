package com.leomac00.reststudy.services;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo; // This was the added import, static to make is easier to refer to
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn; // This was the added import, static to make is easier to refer to

import com.leomac00.reststudy.controllers.PersonController;
import com.leomac00.reststudy.exceptions.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import com.leomac00.reststudy.repositories.PersonRepository;
import com.leomac00.reststudy.data.vo.v1.PersonVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import com.leomac00.reststudy.models.Person;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {
    @Autowired
    PersonRepository personRepository;
    @Autowired
    ModelMapper mapper;

    @Autowired
    PagedResourcesAssembler<PersonVO> assembler;
    private final Logger logger = Logger.getLogger(PersonService.class.getName());
    private final String notFoundMessage = "No person was found for the provided ID!";

    public PersonVO findById(Long id) {
        logger.info("Finding one person");
        var person = getPersonOrElseThrow(id);
        PersonVO vo = mapper.map(person, PersonVO.class);
        vo.add(hateoasLink(id)); // Adds self rel to this returned VO
        return vo;
    }

    public PagedModel<EntityModel<PersonVO>> findAll(Pageable pageable) {
        logger.info("Finding all persons");

        var personPage = personRepository.findAll(pageable);
        var personVosPage = personPage.map(p -> mapper.map(p, PersonVO.class));
        personVosPage.map(p -> p.add(hateoasLink(p.getKey())));

        Link link = linkTo(methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(),
                        "asc",
                        pageable.getPageSize())).withSelfRel();

        return assembler.toModel(personVosPage, link);
    }

    public PersonVO create(PersonVO personVO) {
        if (personVO == null)
            throw new RequiredObjectIsNullException(); // Just make the code throw this exception if body is null, pretty simple
        logger.info("Creating person");

        var entity = mapper.map(personVO, Person.class);
        var vo = mapper.map(personRepository.save(entity), PersonVO.class);

        vo.add(hateoasLink(vo.getKey()));
        return vo;
    }

    @Transactional
    public PersonVO update(PersonVO newPersonData) {
        if (newPersonData == null)
            throw new RequiredObjectIsNullException(); // Just make the code throw this exception if body is null, pretty simple

        logger.info("Updating person");

        var entity = getPersonOrElseThrow(newPersonData.getKey());

        entity.setFirstName(newPersonData.getFirstName());
        entity.setLastName(newPersonData.getLastName());
        entity.setAddress(newPersonData.getAddress());
        entity.setGender(newPersonData.getGender());

        var vo = mapper.map(entity, PersonVO.class);

        vo.add(hateoasLink(personRepository.save(entity).getId()));
        return vo;
    }

    @Transactional
    public void delete(Long id) {
        logger.info("Deleting person");
        var entity = getPersonOrElseThrow(id);
        personRepository.delete(entity);
    }

    @Transactional
    public PersonVO disable(Long id) {
        logger.info("Disabling person with id '" + id);
        if (id == null)
            throw new RequiredObjectIsNullException();

        personRepository.disable(id);

        var entity = getPersonOrElseThrow(id);
        var vo = mapper.map(entity, PersonVO.class);

        vo.add(hateoasLink(id));
        return vo;
    }

    @Transactional
    public PersonVO enable(Long id) {
        logger.info("Enabling person with id '" + id);
        if (id == null)
            throw new RequiredObjectIsNullException();

        personRepository.enable(id);

        var entity = getPersonOrElseThrow(id);
        var vo = mapper.map(entity, PersonVO.class);

        vo.add(hateoasLink(id));
        return vo;
    }

    public PagedModel<EntityModel<PersonVO>> findByPartialFirstName(String partialFirstName, Pageable pageable) {
        logger.info("Finding person with first name like '" + partialFirstName + "'");
        var personPage = personRepository.findByPartialFirstName(partialFirstName, pageable);

        var personVosPage = personPage.map(p -> mapper.map(p, PersonVO.class));
        personVosPage.map(p -> p.add(hateoasLink(p.getKey())));

        Link link = linkTo(methodOn(PersonController.class)
                .findAll(pageable.getPageNumber(),
                        "asc",
                        pageable.getPageSize())).withSelfRel();

        return assembler.toModel(personVosPage, link);
    }

    private Person getPersonOrElseThrow(Long id) {
        return personRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }

    private Link hateoasLink(Long id) {
        return linkTo(methodOn(PersonController.class).findById(id)).withSelfRel();
    }


}
