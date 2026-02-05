package com.library.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.library.entities.Book;
import com.library.persistence.BookRepository;


@RestController
public class BookController {

    private final BookRepository bookRepository;

    private final String BASE_PATH = "/books";

    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping(BASE_PATH)
    List<Book> createBook() {
        return bookRepository.findAll();
    }

    @GetMapping(BASE_PATH)
    List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

}
