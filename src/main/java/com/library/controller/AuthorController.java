package com.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.library.entities.AuthorDTO;
import com.library.services.AuthorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
public class AuthorController {

    private final AuthorService authorService;

    private final String BASE_PATH = "/authors";

    public AuthorController(AuthorService authorService) {
        this.authorService = authorService;

    }

    @GetMapping(BASE_PATH)
    List<AuthorDTO> getAllAuthors() {
        // get all matching entries
        log.info("Received request to get all authors");
        return authorService.getAllAuthors();
    }

    //
    @PostMapping(BASE_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    public void insertAuthors(@RequestBody List<AuthorDTO> authors) {

        log.info("Received request to insert authors: {}", authors);
        authorService.insertAuthors(authors);

    }

}