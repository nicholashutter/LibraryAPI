package com.library.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.library.entities.Author;
import com.library.entities.AuthorDTO;
import com.library.services.AuthorService;

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
        return authorService.getAllAuthors();
    }

    //
    @PostMapping(BASE_PATH)
    public boolean insertAuthors(@RequestBody List<Author> authors) {

        return authorService.insertAuthors(authors);

    }

}