package com.library.services.mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.library.entities.Author;
import com.library.entities.Book;
import com.library.entities.BookDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BookValidator {
    public static Book toBook(BookDTO bookDTO) {

        final UUID DEFAULT_ID = new UUID(0L, 0L);

        Book book = new Book();

        if (bookDTO.authorName() == null) {

            log.info("Book with ID {} has no author. Assigning default author.");

            Author author = new Author();
            author.setId(DEFAULT_ID);
            author.setFirstName("Unknown");
            author.setLastName("Author");

            Book defaultBook = new Book();
            defaultBook.setId(DEFAULT_ID);
            defaultBook.setTitle("Unknown Title");
            defaultBook.setIsbn("000-0-00-000000-0");
            defaultBook.setPublicationDate(LocalDate.of(1900, 1, 1));
            defaultBook.setCreatedAt(LocalDate.now());
            defaultBook.setUpdatedAt(LocalDate.now());

            List<Book> books = List.of(defaultBook);

            author.setBooks(books);

            book.setAuthor(author);

        }

        book.setTitle(bookDTO.title());
        book.setIsbn(bookDTO.isbn());
        book.setPublicationDate(LocalDate.parse(bookDTO.publicationDate()));
        book.setCreatedAt(LocalDate.now());
        book.setUpdatedAt(LocalDate.now());

        return book;
    }
}
