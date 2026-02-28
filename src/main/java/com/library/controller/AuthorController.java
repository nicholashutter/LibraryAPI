package com.library.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.library.entities.AuthorDTO;
import com.library.services.AuthorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
public class AuthorController
{

    private final AuthorService authorService;


    private final String BASE_PATH = "/authors";


    public AuthorController(AuthorService authorService)
    {
        this.authorService = authorService;


    }


    @GetMapping(BASE_PATH + "/{firstName}/{lastName}")
    UUID getAuthorIdByName(@PathVariable String firstName, @PathVariable String lastName)
    {
        log.info("Received request to get author by name: {} {}", firstName, lastName);


        return authorService.findIdByFirstAndLastName(firstName, lastName);
    }


    @GetMapping(BASE_PATH)
    List<AuthorDTO> getAllAuthors()
    {

        log.info("Received request to get all authors");


        return authorService.getAllAuthors();
    }


    @PostMapping(BASE_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertAuthors(@RequestBody List<AuthorDTO> authors)
    {

        log.info("Received request to insert authors: {}", authors);


        authorService.insertAuthors(authors);


    }


    @PutMapping(BASE_PATH + "/{id}")
    @ResponseStatus(HttpStatus.OK )
    public void updateAuthor(@PathVariable UUID id, @RequestBody AuthorDTO author)
    {
        log.info("Received request to update author: {}", author);


        authorService.updateAuthor(id, author);
    }


    @GetMapping(BASE_PATH + "/{id}")
    public AuthorDTO getAuthorById(@PathVariable UUID id)
    {
        log.info("Received request to get author by id: {}", id);


        return authorService.getAuthorById(id);
    }


    @DeleteMapping(BASE_PATH + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthorById(@PathVariable UUID id)
    {
        log.info("Received request to delete author by id: {}", id);


        authorService.deleteById(id);
    }

}
