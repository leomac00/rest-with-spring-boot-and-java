package com.leomac00.restwithspringbootandjava.controllers;

import com.leomac00.restwithspringbootandjava.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import com.leomac00.restwithspringbootandjava.models.Person;
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
	public Person findById(@PathVariable(value="id") String id) {
		return personService.findById(id);
	}
	@GetMapping(path = {"/findAll"},
	produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Person> findAll() {
		return personService.findAll();
	}
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
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
	public void delete(@PathVariable(value="id") String id){
		personService.delete(id);
	}

}