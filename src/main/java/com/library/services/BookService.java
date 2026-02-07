package com.library.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.library.entities.Author;
import com.library.entities.BookDTO;
import com.library.entities.factories.AuthorFactory;
import com.library.mappers.BookMapper;
import com.library.persistence.AuthorRepository;
import com.library.persistence.BookRepository;

import jakarta.transaction.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
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

    @Transactional
    public int deleteByTitle(String title) {
        int rowsAffected = bookRepository.deleteByTitle(title);
        return rowsAffected;
    }

    public BookDTO getByTitle(String title) {
        var book = bookRepository.findByTitle(title);
        return BookMapper.toDTO(book);
    }

    @Transactional
    public boolean updateBook(BookDTO bookDTO) {

        var book = bookRepository.findByTitle(bookDTO.title());

        if (book != null) {

            if (bookDTO.isbn() != null && !bookDTO.isbn().equals(book.getIsbn())) {
                book.setIsbn(bookDTO.isbn());

            }
            if (bookDTO.publicationDate() != null) {
                LocalDate newDate = LocalDate.parse(bookDTO.publicationDate());
                if (!newDate.equals(book.getPublicationDate())) {
                    book.setPublicationDate(newDate);

                }
            }
            if (bookDTO.title() != null && !bookDTO.title().equals(book.getTitle())) {
                book.setTitle(bookDTO.title());

            }

            // bad result of circular dependency 
            if (bookDTO.authorName() != null) {
                Author author = AuthorFactory.createAuthor(bookDTO.authorName(), "", List.of(book), LocalDate.now(), LocalDate.now());
                
                authorRepository.save(author);
                
                book.setAuthor(author);
            }

        } else {
            return false;
        }

        bookRepository.save(book);
        return true;
    }
}
