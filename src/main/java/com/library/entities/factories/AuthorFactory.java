package com.library.entities.factories;

import com.library.entities.Author;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        Author author = new Author();
        author.setFirstName("Unknown");
        author.setLastName("Author");
        author.setCreatedAt(LocalDate.now());
        author.setUpdatedAt(LocalDate.now());

        Book defaultBook = BookFactory.createDefaultBook(author);

        List<Book> books = new ArrayList<>();

        books.add(defaultBook);

        author.setBooks(books);
        return author;
    }
}
