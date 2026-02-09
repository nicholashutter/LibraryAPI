package com.library.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.library.entities.Author;
import com.library.entities.Book;
import com.library.entities.BookDTO;
import com.library.entities.factories.AuthorFactory;
import com.library.mappers.BookMapper;
import com.library.persistence.AuthorRepository;
import com.library.persistence.BookRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookService {

    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    public BookService(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    public boolean insertBooks(List<BookDTO> bookDTOs) {

        bookDTOs.stream().forEach(bookDTO -> {
            Author author = authorMarshaller(bookDTO);
                Book book = BookMapper.toBook(bookDTO);
                book.setAuthor(author);
                bookRepository.save(book);
            
        });
        return true; 
    }

    public UUID findIdByisbn(String isbn) {
        Book book = bookRepository.findIdByisbn(isbn);
        return book.getId();
    }

    public List<BookDTO> getAllBooks() {
        // find all, map to DTO, filter out placeholder books, return list
        return bookRepository.findAll().stream().map(BookMapper::toDTO)
                .filter(book -> !book.firstName().equals("Unknown")).toList();
    }

    @Transactional
    public int deleteByTitle(String title) {
        int rowsAffected = bookRepository.deleteByTitle(title);
        return rowsAffected;
    }

    @Transactional
    public void deleteById(UUID id) {
        bookRepository.deleteById(id);
    }

    public BookDTO getByTitle(String title) {
        var book = bookRepository.findByTitle(title);
        return BookMapper.toDTO(book);
    }

    @Transactional
    public boolean updateBook(BookDTO bookDTO) {

        var book = bookRepository.findIdByisbn(bookDTO.isbn());

        if (book != null) {
            return bookMarshaller(book, bookDTO);
        }

        return false;
    }

    @Transactional
    public boolean updateBook(UUID id, BookDTO bookDTO) {

        Optional<Book> possibleBook = bookRepository.findById(id);

        Book book = possibleBook.orElse(null);

        if (book != null) {
            return bookMarshaller(book, bookDTO);
        }

        return false;
    }

    private boolean bookMarshaller(Book book, BookDTO bookDTO) {

        BookMapper.updateBookFromDTO(bookDTO, book);

        if (bookDTO.firstName() != null) {

            Author author = AuthorFactory.createAuthor(bookDTO.firstName(), "_", List.of(book),
                    LocalDate.now(), LocalDate.now());
            authorRepository.save(author);
            book.setAuthor(author);
        }

        bookRepository.save(book);
        return true;
    }

    private Author authorMarshaller(BookDTO bookDTO) {
        Author author = AuthorFactory.createDefaultAuthor();
        author.setFirstName(bookDTO.firstName());
        author.setLastName(bookDTO.lastName());
        authorRepository.save(author);

        return author;

    }

}
