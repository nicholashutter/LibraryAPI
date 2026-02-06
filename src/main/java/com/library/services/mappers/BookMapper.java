package com.library.services.mappers;

import org.springframework.stereotype.Component;

import com.library.entities.Book;
import com.library.entities.BookDTO;
import com.library.exceptions.ApplicationException;
import com.library.exceptions.Errors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BookMapper {

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
}