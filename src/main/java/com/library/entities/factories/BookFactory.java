package com.library.entities.factories;

import java.time.LocalDate;
import java.util.UUID;

import com.library.entities.Author;
import com.library.entities.Book;

public class BookFactory {

    public static Book createBook(String title, Author author, String isbn, LocalDate publicationDate, LocalDate createdAt, LocalDate updatedAt) {
        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setIsbn(isbn);
        book.setPublicationDate(publicationDate);
        book.setCreatedAt(createdAt);
        book.setUpdatedAt(updatedAt);
        return book;
    }

    public static Book createDefaultBook(Author author) {

        Book book = BookFactory.createBook("Unknown Title", author, "000-0-00-000000-0",
         LocalDate.of(1900, 1, 1), LocalDate.now(), LocalDate.now());
        return book;
    }

}
