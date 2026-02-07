package com.library.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.library.entities.BookDTO;
import com.library.services.BookService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*")
@RestController
public class BookController {

    private final BookService bookService;

    private final String BASE_PATH = "/books";

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping(BASE_PATH)
    public void insertBooks(@RequestBody List<BookDTO> books) {

        log.info("Received request to insert books: {}", books);
        bookService.insertBooks(books);
    }

    @GetMapping(BASE_PATH)
    @ResponseStatus(HttpStatus.CREATED)
    List<BookDTO> getAllBooks() {
        // get all matching entries
        log.info("Received request to get all books");
        return bookService.getAllBooks();
    }

    @DeleteMapping(BASE_PATH)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public int deleteByTitle(@RequestBody BookDTO bookDTO) {
        log.info("Received request to delete book with title: {}", bookDTO.title());
        int rowsAffected = bookService.deleteByTitle(bookDTO.title());
        return rowsAffected;
    }

    @PutMapping(BASE_PATH)
    @ResponseStatus(HttpStatus.OK)
    public void updateBook(@RequestBody BookDTO bookDTO) {
        log.info("Received request to update book with title: {}", bookDTO.title());
        bookService.updateBook(bookDTO);
    }

}
