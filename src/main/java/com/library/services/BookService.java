package com.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.entities.Book;
import com.library.persistence.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public boolean insertBooks(List<Book> books) {
        books.stream().forEach(book -> bookRepository.save(book));
        return true; // we only return true for success
    }

    public List<Book> getAllBooks() {
        // get all matching entries
        return bookRepository.findAll();
    }
}
