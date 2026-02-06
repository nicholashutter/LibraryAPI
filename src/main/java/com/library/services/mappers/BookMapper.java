package com.library.services.mappers;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.library.entities.Author;
import com.library.entities.Book;
import com.library.entities.BookDTO;
import com.library.exceptions.ApplicationException;
import com.library.exceptions.Errors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BookMapper {

    final static UUID DEFAULT_ID = new UUID(0L, 0L);

    public static BookDTO toDTO(Book book) {

        BookDTO dto = null;

        try {
            if (book == null) {
                throw new ApplicationException(Errors.NULL_ARGUMENT);
            }

            String authorName = "Unknown Author";
            if (book.getAuthor() != null) {
                authorName = book.getAuthor().getFirstName() + " " + book.getAuthor().getLastName();
            }

            dto = new BookDTO(
                    book.getTitle(),
                    book.getIsbn(),
                    book.getPublicationDate() != null ? book.getPublicationDate().toString() : null,
                    authorName);

        } catch (ApplicationException ex) {
            log.error("Mapping error: {}", ex.getMessage());
        }

        return dto;
    }

    public static Book toBook(BookDTO bookDTO) {

        Book book = new Book();

        if (bookDTO.authorName() == null) {

            log.info("Book with ID {} has no author. Assigning default author.");

            Author author = new Author();
            author.setId(BookMapper.DEFAULT_ID);
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

    public static Book validateBook(Book book) {

        Book validatedBook = new Book();

        if (book.getAuthor() == null) {
            log.info("Book with ID {} has no author. Assigning default author.", book.getId());

            Author author = new Author();
            author.setId(BookMapper.DEFAULT_ID);
            author.setFirstName("Unknown");
            author.setLastName("Author");

            Book defaultBook = new Book();
            defaultBook.setId(BookMapper.DEFAULT_ID);

            author.setBooks(List.of(defaultBook));
            book.setAuthor(author);

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
        }
        book.setTitle(book.getTitle());
        book.setIsbn(book.getIsbn());

        if (book.getPublicationDate() != null) {
            book.setPublicationDate(LocalDate.parse(book.getPublicationDate().toString()));
        }

        book.setCreatedAt(LocalDate.now());
        book.setUpdatedAt(LocalDate.now());

        return book;
    }
}
