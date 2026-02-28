package com.library.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.library.entities.Author;
import com.library.entities.Book;
import com.library.entities.BookDTO;
import com.library.entities.factories.AuthorFactory;
import com.library.mappers.BookMapper;
import com.library.persistence.AuthorRepository;
import com.library.persistence.BookRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BookService
{

    private final BookRepository bookRepository;


    private final AuthorRepository authorRepository;


    public BookService(BookRepository bookRepository, AuthorRepository authorRepository)
    {
        this.bookRepository = bookRepository;


        this.authorRepository = authorRepository;
    }


    public boolean insertBooks(List<BookDTO> bookDTOs)
    {

        bookDTOs.stream().forEach(bookDTO ->
        {

            Book book = BookMapper.toBook(bookDTO);


            bookRepository.save(book);


        });


        return true;
    }


    public UUID findIdByisbn(String isbn)
    {
        Book book = bookRepository.findIdByisbn(isbn);


        if (book == null)
        {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " not found");
        }


        return book.getId();
    }


    public List<BookDTO> getAllBooks()
    {

        List<BookDTO> books = new ArrayList<>();


        bookRepository.findAll().stream().forEach(book ->
        {
            if (!book.getAuthor().getFirstName().equals("Unknown") &&


                    !book.getIsbn().contains("000") &&


                    !book.getAuthor().getLastName().equals("Book"))
            {
                BookDTO dto = BookMapper.toDTO(book, book.getAuthor());


                books.add(dto);
            }
        });


        return books;
    }


    @Transactional
    public int deleteByTitle(String title)
    {
        int rowsAffected = bookRepository.deleteByTitle(title);


        return rowsAffected;
    }


    public BookDTO getById(UUID id)
    {
        return bookRepository.findById(id)
                .map(book -> BookMapper.toDTO(book, book.getAuthor()))
                .orElse(null);
    }


    @Transactional
    public void deleteById(UUID id)
    {
        bookRepository.deleteById(id);
    }


    @Transactional
    public boolean updateBook(BookDTO bookDTO)
    {

        var book = bookRepository.findIdByisbn(bookDTO.isbn());


        if (book != null)
        {
            return bookMarshaller(book, bookDTO);
        }


        return false;
    }


    @Transactional
    public boolean updateBook(UUID id, BookDTO bookDTO)
    {

        Optional<Book> possibleBook = bookRepository.findById(id);


        Book book = possibleBook.orElse(null);


        if (book != null)
        {
            return bookMarshaller(book, bookDTO);
        }


        return false;
    }


    private boolean bookMarshaller(Book book, BookDTO bookDTO)
    {

        BookMapper.updateBookFromDTO(bookDTO, book);


        if (bookDTO.firstName() != null)
        {

            Author author = AuthorFactory.createAuthor(bookDTO.firstName(),
                    bookDTO.lastName(),
                    List.of(book),
                    LocalDate.now(),
                    LocalDate.now());


            authorRepository.save(author);


            book.setAuthor(author);
        }


        bookRepository.save(book);


        return true;
    }

}

