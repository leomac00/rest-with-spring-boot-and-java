package com.leomac00.reststudy.services;

import com.leomac00.reststudy.data.vo.v1.PersonVO;
import com.leomac00.reststudy.exceptions.RequiredObjectIsNullException;
import com.leomac00.reststudy.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import com.leomac00.reststudy.repositories.BookRepository;
import com.leomac00.reststudy.controllers.BookController;
import com.leomac00.reststudy.data.vo.v1.BookVO;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.stereotype.Service;
import com.leomac00.reststudy.models.Book;
import org.springframework.hateoas.Link;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class BookService {
    @Autowired
    BookRepository repository;
    @Autowired
    ModelMapper mapper;
    @Autowired
    PagedResourcesAssembler<BookVO> assembler;

    private final Logger logger = Logger.getLogger(BookService.class.getName());
    private final String notFoundMessage = "No book was found for the provided ID!";

    public BookVO findById(Long id) {
        logger.info("Finding book using ID: " + id);
        var book = getBookOrElseThrow(id);
        var vo = mapper.map(book, BookVO.class);
        vo.add(hateoasLink(id));
        return vo;
    }

    public PagedModel<EntityModel<BookVO>> findAll(Pageable pageable) {
        logger.info("Finding all books...: ");

        var bookPage = repository.findAll(pageable);
        var bookVosPage = bookPage.map(b -> mapper.map(b, BookVO.class));
        bookVosPage.map(b -> b.add(hateoasLink(b.getKey())));

        Link link = linkTo(methodOn(BookController.class).findAll(pageable.getPageNumber(), "asc", pageable.getPageSize())).withSelfRel();

        return assembler.toModel(bookVosPage, link);
    }

    public BookVO create(BookVO bookReceived) {
        if (bookReceived == null) throw new RequiredObjectIsNullException();
        logger.info("Creating book...");
        var entity = mapper.map(bookReceived, Book.class);
        var result = mapper.map(repository.save(entity), BookVO.class);
        return result.add(hateoasLink(result.getKey()));
    }

    public BookVO update(BookVO bookReceived) {
        if (bookReceived == null) throw new RequiredObjectIsNullException();
        logger.info("Updating book " + bookReceived.getKey() + "...");

        var entity = getBookOrElseThrow(bookReceived.getKey());
        entity.setTitle(bookReceived.getTitle());
        entity.setPrice(bookReceived.getPrice());
        entity.setAuthor(bookReceived.getAuthor());
        entity.setLaunchDate(bookReceived.getLaunchDate());

        var vo = mapper.map(repository.save(entity), BookVO.class);
        return vo.add(hateoasLink(vo.getKey()));
    }

    public void delete(Long id) {
        logger.info("Deleting book  with id = " + id + " ...");
        var entity = getBookOrElseThrow(id);
        repository.delete(entity);
    }

    private Book getBookOrElseThrow(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(notFoundMessage));
    }

    private Link hateoasLink(Long id) {
        return linkTo(methodOn(BookController.class).findById(id)).withSelfRel();
    }

}
