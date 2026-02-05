package com.library.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entities.Author;
import com.library.persistence.AuthorRepository;

@RestController
public class AuthorController {

    private final AuthorRepository authorRepository;

    private final String BASE_PATH = "/authors";

    public AuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    /*
     * @PostMapping(BASE_PATH)
     * List<Book> createAuthor() {
     * return authorRepository.findAll();
     * }
     */

    @GetMapping(BASE_PATH)
    List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

}