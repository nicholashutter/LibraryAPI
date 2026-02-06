package com.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.entities.BookDTO;
import com.library.mappers.BookMapper;
import com.library.persistence.BookRepository;

@Service
public class BookService {

    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public boolean insertBooks(List<BookDTO> books) {
        books.stream().map(BookMapper::toBook).forEach(book -> bookRepository.save(book));
        return true; // we only return true for success
    }

    public List<BookDTO> getAllBooks() {
        // find all, map to DTO, filter out placeholder books, return list
        return bookRepository.findAll().stream().map(BookMapper::toDTO)
                .filter(book -> !book.isbn().equals("000-0000000000")).toList();
    }
}
