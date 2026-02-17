package com.library.entities.factories;

import com.library.entities.Author;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.library.entities.Book;

public class AuthorFactory {
    public static Author createAuthor(String firstName, String lastName, List<Book> books, LocalDate createdAt,
            LocalDate updatedAt) {
        Author author = new Author();
        author.setFirstName(firstName);
        author.setLastName(lastName);
        author.setBooks(books);
        author.setCreatedAt(createdAt);
        author.setUpdatedAt(updatedAt);
        return author;
    }

    public static Author createDefaultAuthor() {
        UUID defaultAuthorId = new UUID(0L, 0L);

        List<Book> books = new ArrayList<>();

        Author author = AuthorFactory.createAuthor("Unknown", "Author", books, LocalDate.now(), LocalDate.now());
        author.setId(defaultAuthorId);

        books.add(BookFactory.createDefaultBook(author));

        author.setBooks(books);
        return author;
    }
}
