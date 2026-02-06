package com.library.services.mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.library.entities.Author;
import com.library.entities.AuthorDTO;
import com.library.entities.Book;
import com.library.exceptions.ApplicationException;
import com.library.exceptions.Errors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorMapper {

    final static UUID DEFAULT_ID = new UUID(0L, 0L);

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
        Author author = new Author();

        author.setId(AuthorMapper.DEFAULT_ID);
        author.setFirstName(authorDTO.firstName());
        author.setLastName(authorDTO.lastName());
        author.setCreatedAt(LocalDate.now());
        author.setUpdatedAt(LocalDate.now());

        Book defaultBook = new Book();
        defaultBook.setId(DEFAULT_ID);
        defaultBook.setTitle("Unknown Title");
        defaultBook.setIsbn("000-0-00-000000-0");
        defaultBook.setPublicationDate(LocalDate.of(1900, 1, 1));
        defaultBook.setCreatedAt(LocalDate.now());
        defaultBook.setUpdatedAt(LocalDate.now());

        List<Book> books = List.of(defaultBook);

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

}
