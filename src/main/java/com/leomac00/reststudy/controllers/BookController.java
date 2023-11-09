package com.leomac00.reststudy.controllers;

import com.leomac00.reststudy.Utils.MyMediaType;
import com.leomac00.reststudy.data.vo.v1.BookVO;
import com.leomac00.reststudy.data.vo.v1.PersonVO;
import com.leomac00.reststudy.models.Book;
import com.leomac00.reststudy.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Book", description = "Endpoints for managing books")
@RestController
@RequestMapping("api/book/v1")
public class BookController {

    @Autowired
    BookService service;

    @GetMapping(path = {"/{id}"},
            produces = {MyMediaType.APPLICATION_JSON, MyMediaType.APPLICATION_XML, MyMediaType.APPLICATION_YAML})
    @Operation(
            summary = "Finds one book based on provided id.",
            description = "Finds one book based on provided id.",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MyMediaType.APPLICATION_JSON,
                                            array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @CrossOrigin(origins = {"http://leomac00.com", "http://localhost:8080"})
    //This limits the requests for these 2 origins
    public BookVO findById(@PathVariable(name = "id") Long id) {
        return service.findById(id);
    }


    @GetMapping(path = {"/findAll"},
            produces = {MyMediaType.APPLICATION_JSON,
                    MyMediaType.APPLICATION_YAML,
                    MyMediaType.APPLICATION_XML})
    @Operation(
            summary = "Finds all Books.",
            description = "Finds all Books.",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MyMediaType.APPLICATION_JSON,

                                            array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public ResponseEntity<PagedModel<EntityModel<BookVO>>> findAll(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "limit", defaultValue = "12") Integer limit
    ) {
        Pageable pageable = direction.equalsIgnoreCase("asc")
                ? PageRequest.of(page, limit, Sort.by("id").ascending())
                : PageRequest.of(page, limit, Sort.by("id").descending());

        return ResponseEntity.ok(service.findAll(pageable));
    }

    @PostMapping(consumes = {MyMediaType.APPLICATION_JSON,
            MyMediaType.APPLICATION_YAML,
            MyMediaType.APPLICATION_XML},
            produces = {MyMediaType.APPLICATION_JSON,
                    MyMediaType.APPLICATION_YAML,
                    MyMediaType.APPLICATION_XML})
    @Operation(
            summary = "Creates a Book entry in the database based on it's body data.",
            description = "Creates a Book entry in the database based on it's body data.",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MyMediaType.APPLICATION_JSON,

                                            array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    @CrossOrigin(origins = {"http://localhost:8080"}) //This limits the request for this single origin
    public BookVO create(@RequestBody BookVO bookVO) {
        return service.create(bookVO);
    }

    @PutMapping(consumes = {MyMediaType.APPLICATION_JSON,
            MyMediaType.APPLICATION_YAML,
            MyMediaType.APPLICATION_XML},
            produces = {MyMediaType.APPLICATION_JSON,
                    MyMediaType.APPLICATION_YAML,
                    MyMediaType.APPLICATION_XML})
    @Operation(
            summary = "Updates a Book entry in the database based on it's body data.",
            description = "Updates a Book entry in the database based on it's body data.",
            tags = {"Book"},
            responses = {
                    @ApiResponse(
                            description = "Success",
                            responseCode = "200",
                            content = {
                                    @Content(
                                            mediaType = MyMediaType.APPLICATION_JSON,

                                            array = @ArraySchema(schema = @Schema(implementation = BookVO.class))
                                    )
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public BookVO update(@RequestBody BookVO book) {
        return service.update(book);
    }

    @DeleteMapping(path = {"/{id}"})
    @Operation(
            summary = "Deletes a Book entry in the database based on a provided ID.",
            description = "Deletes a Book entry in the database based on a provided ID.",
            tags = {"Book"},
            responses = {
                    @ApiResponse(description = "No Content", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public void delete(@PathVariable(name = "id") Long id) {
        service.delete(id);
    }
}
