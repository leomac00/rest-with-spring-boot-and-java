package com.leomac00.reststudy.controllers;

import com.leomac00.reststudy.Utils.MyMediaType;
import com.leomac00.reststudy.data.vo.v1.PersonVO;
import com.leomac00.reststudy.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/person/v1") // Creates a route to "localhost:8080/person"
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(path = {"/hi"})
    public String hi() {
        return "Hi";
    }

    @GetMapping(path = {"/{id}"},
            produces = {MyMediaType.APPLICATION_JSON,
                    MyMediaType.APPLICATION_YAML,
                    MyMediaType.APPLICATION_XML})
    public PersonVO findById(@PathVariable(value = "id") Long id) {
        return personService.findById(id);
    }

    @GetMapping(path = {"/findAll"},
            produces = {MyMediaType.APPLICATION_JSON,
                    MyMediaType.APPLICATION_YAML,
                    MyMediaType.APPLICATION_XML})
    public List<PersonVO> findAll() {
        return personService.findAll();
    }

    @PostMapping(consumes = {MyMediaType.APPLICATION_JSON,
            MyMediaType.APPLICATION_YAML,
            MyMediaType.APPLICATION_XML}, // Consumes and Produces are optional but when using them it makes the swagger docs more complete
            produces = {MyMediaType.APPLICATION_JSON,
                    MyMediaType.APPLICATION_YAML,
                    MyMediaType.APPLICATION_XML})
    public PersonVO create(@RequestBody PersonVO person) {
        return personService.create(person);
    }

    @PutMapping(consumes = {MyMediaType.APPLICATION_JSON,
            MyMediaType.APPLICATION_YAML,
            MyMediaType.APPLICATION_XML},
            produces = {MyMediaType.APPLICATION_JSON,
                    MyMediaType.APPLICATION_YAML,
                    MyMediaType.APPLICATION_XML})
    public PersonVO update(@RequestBody PersonVO person) {
        return personService.update(person);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) { //Now it return the ResponseEntity.noContent for better docs when we start using swagger
        personService.delete(id);

        return ResponseEntity.noContent().build();
    }

}