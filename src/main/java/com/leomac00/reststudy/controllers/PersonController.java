package com.leomac00.reststudy.controllers;

import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import com.leomac00.reststudy.services.PersonService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import com.leomac00.reststudy.data.vo.v1.PersonVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.*;
import com.leomac00.reststudy.Utils.MyMediaType;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;


@Tag(name = "People", description = "Endpoints for Managing People [@Tag]")
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
    @Operation( // From here we define what this endpoint does and how it behaves, we insert the descriptions here using the following annotations
            summary = "Finds one person based on provided id.[@Operation.summary]",
            description = "Finds one person based on provided id.[@Operation.description]",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MyMediaType.APPLICATION_JSON,

                                            array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public PersonVO findById(@PathVariable(value = "id") Long id) {
        return personService.findById(id);
    }

    @GetMapping(path = {"/findAll"},
            produces = {MyMediaType.APPLICATION_JSON,
                    MyMediaType.APPLICATION_YAML,
                    MyMediaType.APPLICATION_XML})
    @Operation( // From here we define what this endpoint does and how it behaves, we insert the descriptions here using the following annotations
            summary = "Finds all People.[@Operation.summary]",
            description = "Finds all People.[@Operation.description]",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MyMediaType.APPLICATION_JSON,

                                            array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "limit", defaultValue = "12") Integer limit
    ) {
        Pageable pageable = direction.equalsIgnoreCase("asc")
                ? PageRequest.of(page, limit, Sort.by("id").ascending())
                : PageRequest.of(page, limit, Sort.by("id").descending());

        return ResponseEntity.ok(personService.findAll(pageable));
    }

    @GetMapping(path = {"/findByPartialName/{partialFirstName}"},
            produces = {MyMediaType.APPLICATION_JSON,
                    MyMediaType.APPLICATION_YAML,
                    MyMediaType.APPLICATION_XML})
    @Operation( // From here we define what this endpoint does and how it behaves, we insert the descriptions here using the following annotations
            summary = "Finds Person by partial name.",
            description = "Finds Person by partial name.",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MyMediaType.APPLICATION_JSON,

                                            array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PagedModel<EntityModel<PersonVO>>> findByPartialName(
            @PathVariable("partialFirstName") String partialFirstName,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "limit", defaultValue = "12") Integer limit
    ) {
        var sortDirection = direction.equalsIgnoreCase("asc")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, limit, Sort.by(sortDirection, "id"));

        return ResponseEntity.ok(personService.findByPartialFirstName(partialFirstName, pageable));
    }

    @PostMapping(consumes = {MyMediaType.APPLICATION_JSON,
            MyMediaType.APPLICATION_YAML,
            MyMediaType.APPLICATION_XML}, // Consumes and Produces are optional but when using them it makes the swagger docs more complete
            produces = {MyMediaType.APPLICATION_JSON,
                    MyMediaType.APPLICATION_YAML,
                    MyMediaType.APPLICATION_XML})
    @Operation( // From here we define what this endpoint does and how it behaves, we insert the descriptions here using the following annotations
            summary = "Creates a Person entry in the database based on it's body data.[@Operation.summary]",
            description = "Creates a Person entry in the database based on it's body data.[@Operation.description]",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MyMediaType.APPLICATION_JSON,

                                            array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public PersonVO create(@RequestBody PersonVO person) {
        return personService.create(person);
    }

    @PutMapping(consumes = {MyMediaType.APPLICATION_JSON,
            MyMediaType.APPLICATION_YAML,
            MyMediaType.APPLICATION_XML},
            produces = {MyMediaType.APPLICATION_JSON,
                    MyMediaType.APPLICATION_YAML,
                    MyMediaType.APPLICATION_XML})
    @Operation( // From here we define what this endpoint does and how it behaves, we insert the descriptions here using the following annotations
            summary = "Updates a Person entry in the database based on it's body data.[@Operation.summary]",
            description = "Updates a Person entry in the database based on it's body data.[@Operation.description]",
            tags = {"People"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MyMediaType.APPLICATION_JSON,

                                            array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public PersonVO update(@RequestBody PersonVO person) {
        return personService.update(person);
    }

    @DeleteMapping(path = {"/{id}"})
    @Operation( // From here we define what this endpoint does and how it behaves, we insert the descriptions here using the following annotations
            summary = "Deletes a Person entry in the database based on a provided ID.",
            description = "Deletes a Person entry in the database based on a provided ID.",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<?> delete(@PathVariable(value = "id") Long id) { //Now it return the ResponseEntity.noContent for better docs when we start using swagger
        personService.delete(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping(path = {"/disable/{id}"})
    @Operation(
            summary = "Disables person based on a provided ID.",
            description = "Disables person based on a provided ID.",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public PersonVO disable(@PathVariable(value = "id") Long id) {
        return personService.disable(id);
    }

    @PatchMapping(path = {"/enable/{id}"})
    @Operation(
            summary = "Enables person based on a provided ID.",
            description = "Enables person based on a provided ID.",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public PersonVO enable(@PathVariable(value = "id") Long id) {
        return personService.enable(id);
    }
}