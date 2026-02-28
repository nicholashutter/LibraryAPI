package com.library.mappers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.library.entities.Author;
import com.library.entities.Book;
import com.library.entities.BookDTO;
import com.library.entities.factories.BookFactory;
import com.library.exceptions.ApplicationException;
import com.library.exceptions.Errors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BookMapper
{


    public static BookDTO toDTO(Book book, Author author)
    {

        BookDTO dto = null;


        try
        {
            if (book == null)
            {
                throw new ApplicationException(Errors.NULL_ARGUMENT);
            }


            dto = new BookDTO(
                    book.getId(),
                    book.getTitle(),
                    book.getIsbn(),
                    book.getPublicationDate() != null ? book.getPublicationDate().toString() : null,
                    author.getFirstName(),
                    author.getLastName());


        }
        catch (ApplicationException ex)
        {
            log.error("Mapping error: {}", ex.getMessage());
        }


        return dto;
    }


    public static Book toBook(BookDTO bookDTO)
    {

        Author author = BookMapper.authorMarshaller(bookDTO);


        Book book = BookFactory.createBook(bookDTO.title(),
                author,
                bookDTO.isbn(),
                LocalDate.parse(bookDTO.publicationDate()),
                LocalDate.now(),
                LocalDate.now());


        if (bookDTO.id() != null)
        {
            book.setId(bookDTO.id());
        }


        List<Book> books = List.of(book);


        author.setBooks(books);


        book.setAuthor(author);


        return book;
    }


    public static void updateBookFromDTO(BookDTO dto, Book book)
    {
        if (dto.title() != null)
        {
            book.setTitle(dto.title());
        }


        if (dto.isbn() != null)
        {
            book.setIsbn(dto.isbn());
        }


        if (dto.publicationDate() != null)
        {
            book.setPublicationDate(LocalDate.parse(dto.publicationDate()));
        }
    }


    private static Author authorMarshaller(BookDTO bookDTO)
    {
        Author author = com.library.entities.factories.AuthorFactory.createAuthor(bookDTO.firstName(),
                bookDTO.lastName(),
                new ArrayList<>(),
                LocalDate.now(),
                LocalDate.now());


        return author;
    }

}
