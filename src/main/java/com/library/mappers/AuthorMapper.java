package com.library.mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

import com.library.entities.Author;
import com.library.entities.AuthorDTO;
import com.library.entities.Book;
import com.library.entities.BookDTO;
import com.library.entities.factories.AuthorFactory;
import com.library.entities.factories.BookFactory;
import com.library.exceptions.ApplicationException;
import com.library.exceptions.Errors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorMapper {

    final static String DEFAULT_ISBN = "000-0-00-000000-0";
    final static LocalDate DEFAULT_PUBLICATION_DATE = LocalDate.of(1900, 1, 1);

    public static AuthorDTO toDTO(Author author) {

        AuthorDTO dto = null;

        try {
            if (author == null) {
                throw new ApplicationException(Errors.NULL_ARGUMENT);
            }

            dto = new AuthorDTO(author.getFirstName(), author.getLastName(),
                    author.getBooks().stream().map(book -> BookMapper.toDTO(book, author)).toList());

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

        if (authorDTO.books().isEmpty() || authorDTO.books() == null) {
            books.add(defaultBook);

            return author;
        } else {
            for (BookDTO bookDTO : authorDTO.books()) {
                String isbn = bookDTO.isbn();
                if (bookDTO.isbn().isEmpty()) {
                    isbn = AuthorMapper.generateIsbn();
                }
                Book book = BookFactory.createBook(bookDTO.title(), author, isbn, DEFAULT_PUBLICATION_DATE,
                        LocalDate.now(), LocalDate.now());
                books.add(book);

            }
        }

        for (Book book : author.getBooks()) {
            BookMapper.validateBook(book);
        }

        author.setBooks(books);

        for (Book book : author.getBooks()) {
            book.setAuthor(author);
        }
        return author;
    }

    private static String generateIsbn() {
        int[] digits = new Random().ints(12, 0, 10).toArray();

        int sum = IntStream.range(0, 12)
                .map(i -> digits[i] * (i % 2 == 0 ? 1 : 3))
                .sum();

        int checksum = (10 - (sum % 10)) % 10;

        StringBuilder isbn = new StringBuilder("978"); // Common prefix
        IntStream.range(3, 12).forEach(i -> isbn.append(digits[i]));
        return isbn.append(checksum).toString();
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

        if (dto.books() != null) {
            updateBooksFromTitles(dto.books(), author);
        }

        author.setUpdatedAt(LocalDate.now());
        author.setCreatedAt(LocalDate.now());

        return author;
    }

    private static void updateBooksFromTitles(List<BookDTO> bookDTOs, Author author) {
        List<Book> existingBooks = author.getBooks();
        if (existingBooks == null) {
            existingBooks = new ArrayList<>();
            author.setBooks(existingBooks);
        }

        existingBooks.clear();
        for (BookDTO bookDTO : bookDTOs) {
            String isbn = DEFAULT_ISBN;
            if (bookDTO.isbn().isEmpty()) {
                isbn = AuthorMapper.generateIsbn();
            }
            Book book = BookFactory.createBook(bookDTO.title(), author, isbn, DEFAULT_PUBLICATION_DATE,
                    LocalDate.now(), LocalDate.now());

            existingBooks.add(book);
        }
    }
}
