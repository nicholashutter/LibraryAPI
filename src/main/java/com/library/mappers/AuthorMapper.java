package com.library.mappers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.library.entities.Author;
import com.library.entities.AuthorDTO;
import com.library.entities.Book;
import com.library.exceptions.ApplicationException;
import com.library.exceptions.Errors;

import com.library.entities.factories.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorMapper {

        final static UUID DEFAULT_ID = new UUID(0L, 0L);
        final static String DEFAULT_ISBN = "000-0-00-000000-0";
        final static LocalDate DEFAULT_PUBLICATION_DATE = LocalDate.of(1900, 1, 1);

    public static AuthorDTO toDTO(Author author) {

        AuthorDTO dto = null;

        try {
            if (author == null) {
                throw new ApplicationException(Errors.NULL_ARGUMENT);
            }

            dto = new AuthorDTO(author.getFirstName(), author.getLastName(),
                    author.getBooks().stream().map(book -> book.getTitle()).toArray(size -> new String[size]));

        } catch (ApplicationException ex) {
            log.error(ex.getMessage());
        }

        return dto;

    }

    public static Author toAuthor(AuthorDTO authorDTO) {
        Author author = AuthorFactory.createDefaultAuthor();
        author.setFirstName(authorDTO.firstName());
        author.setLastName(authorDTO.lastName());

        Book defaultBook = BookFactory.createDefaultBook(author);

        List<Book> books = new ArrayList<>();

        books.add(defaultBook);

        if (author.getBooks() == null || author.getBooks().isEmpty()) {
            author.setBooks(books);
        } else {
            for (Book book : author.getBooks()) {
                BookMapper.validateBook(book);
            }

            author.setBooks(books);

        }
        return author;
    }

    public static Author updateFromDTO(AuthorDTO dto, Author author) {
        if (dto == null || author == null)
            return author;

        if (dto.firstName() != null && !dto.firstName().equals(author.getFirstName())) {
            author.setFirstName(dto.firstName());
        }

        if (dto.lastName() != null && !dto.lastName().equals(author.getLastName())) {
            author.setLastName(dto.lastName());
        }

        if (dto.bookTitles() != null) {
            updateBooksFromTitles(dto.bookTitles(), author);
        }

        author.setUpdatedAt(LocalDate.now());
        author.setCreatedAt(LocalDate.now());

        return author;
    }

    private static void updateBooksFromTitles(String[] titles, Author author) {
        List<Book> existingBooks = author.getBooks();
        if (existingBooks == null) {
            existingBooks = new ArrayList<>();
            author.setBooks(existingBooks);
        }

        existingBooks.clear();
        for (String title : titles) {
            Book book = BookFactory.createBook(title, author, DEFAULT_ISBN, DEFAULT_PUBLICATION_DATE, 
                LocalDate.now(), LocalDate.now());
            
            existingBooks.add(book);
        }
    }
}
