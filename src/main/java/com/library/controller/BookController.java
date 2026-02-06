package com.library.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.library.entities.Book;

import com.library.services.BookService;

@RestController
public class BookController {

    private final BookService bookService;

    private final String BASE_PATH = "/books";

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(BASE_PATH)
    public boolean insertBooks(@RequestBody List<Book> books) {
        return bookService.insertBooks(books);
    }

    @GetMapping(BASE_PATH)
    List<Book> getAllBooks() {
        // get all matching entries
        return bookService.getAllBooks();
    }

}
