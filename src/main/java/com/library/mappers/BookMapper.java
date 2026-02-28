package com.library.mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.library.entities.Author;
import com.library.entities.Book;
import com.library.entities.BookDTO;
import com.library.exceptions.ApplicationException;
import com.library.exceptions.Errors;

import com.library.entities.factories.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BookMapper {


    public static BookDTO toDTO(Book book, Author author) {

        BookDTO dto = null;

        try {
            if (book == null) {
                throw new ApplicationException(Errors.NULL_ARGUMENT);
            }

            dto = new BookDTO(
                    book.getTitle(),
                    book.getIsbn(),
                    book.getPublicationDate() != null ? book.getPublicationDate().toString() : null, 
                    author.getFirstName(), author.getLastName());

        } catch (ApplicationException ex) {
            log.error("Mapping error: {}", ex.getMessage());
        }

        return dto;
    }

    public static Book toBook(BookDTO bookDTO) {

        Author author = BookMapper.authorMarshaller(bookDTO);

        Book book = BookFactory.createBook(bookDTO.title(), author, bookDTO.isbn(),
                LocalDate.parse(bookDTO.publicationDate()),
                LocalDate.now(), LocalDate.now());
        List<Book> books = List.of(book);
        author.setBooks(books);
        book.setAuthor(author);

        return book;
    }

    public static Book validateBook(Book book) {

        Author author = AuthorFactory.createDefaultAuthor();

        if (book.getAuthor() == null) {

            log.info("Book with ID {} has no author. Assigning default author.", book.getId());

            book.setAuthor(author);
        }

        author.getBooks().stream().forEach(b -> {
            if (b.getId() == null) {
                b.setId(UUID.randomUUID());
            }
            if (b.getCreatedAt() == null) {
                b.setCreatedAt(LocalDate.now());
            }
            if (b.getUpdatedAt() == null) {
                b.setUpdatedAt(LocalDate.now());
            }
            if (b.getAuthor() == null) {
                b.setAuthor(author);
            }
            if (b.getTitle() == null) {
                b.setTitle("Unknown Title");
            }
            if (b.getIsbn() == null) {
                b.setIsbn("000-0-00-000000-0");
            }
            if (b.getPublicationDate() == null) {
                b.setPublicationDate(LocalDate.of(1900, 1, 1));
            }
        });

        return book;
    }

    public static void updateBookFromDTO(BookDTO dto, Book book) {
        if (dto.title() != null)
            book.setTitle(dto.title());
        if (dto.isbn() != null)
            book.setIsbn(dto.isbn());
        if (dto.publicationDate() != null) {
            book.setPublicationDate(LocalDate.parse(dto.publicationDate()));
        }
    }

    private static Author authorMarshaller(BookDTO bookDTO) {

        List<Book> books = new ArrayList<>();
        Book book = BookFactory.createDefaultBook(AuthorFactory.createDefaultAuthor());
        books.add(book);
        Author author = AuthorFactory.createAuthor(bookDTO.firstName(), bookDTO.lastName(), 
        books, LocalDate.now(), LocalDate.now());
        return author;

    }

}
