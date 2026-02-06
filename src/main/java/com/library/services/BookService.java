package com.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.entities.BookDTO;
import com.library.persistence.BookRepository;
import com.library.services.mappers.BookValidator;
import com.library.services.mappers.BookMapper;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public boolean insertBooks(List<BookDTO> books) {
        books.stream().map(BookValidator::toBook).forEach(book -> bookRepository.save(book));
        return true; // we only return true for success
    }

    public List<BookDTO> getAllBooks() {
        // get all matching entries
        return bookRepository.findAll().stream().map(BookMapper::toDTO).toList();
    }
}
