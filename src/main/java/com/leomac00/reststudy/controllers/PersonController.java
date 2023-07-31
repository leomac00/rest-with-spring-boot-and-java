package com.leomac00.reststudy.controllers;

import com.leomac00.reststudy.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import com.leomac00.reststudy.models.Person;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;

import java.util.List;


@RestController
@RequestMapping("/person") // Creates a route to "localhost:8080/person"
public class PersonController {

    @Autowired
    private PersonService personService;

    @GetMapping(path = {"/hi"})
    public String hi() {
        return "Hi";
    }

    @GetMapping(path = {"/{id}"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person findById(@PathVariable(value = "id") Long id) {
        return personService.findById(id);
    }

    @GetMapping(path = {"/findAll"},
            produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Person> findAll() {
        return personService.findAll();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, // Consumes and Produces are optional but when using them it makes the swagger docs more complete
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person create(@RequestBody Person person) {
        return personService.create(person);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Person update(@RequestBody Person person) {
        return personService.update(person);
    }

    @DeleteMapping(path = {"/{id}"})
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) { //Now it return the ResponseEntity.noContent for better docs when we start using swagger
        personService.delete(id);

        return ResponseEntity.noContent().build();
    }

}