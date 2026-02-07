package com.library.entities.factories;

import java.time.LocalDate;

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
        Book book = new Book();
        book.setTitle("Unknown Title");
        book.setAuthor(null);
        book.setIsbn("000-0-00-000000-0");
        book.setPublicationDate(LocalDate.of(1900, 1, 1));
        book.setCreatedAt(LocalDate.now());
        book.setUpdatedAt(LocalDate.now());
        book.setAuthor(author);
        return book;
    }

}
