package com.library.services;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.library.entities.Author;
import com.library.entities.AuthorDTO;
import com.library.entities.Book;
import com.library.entities.factories.AuthorFactory;
import com.library.entities.factories.BookFactory;
import com.library.exceptions.Errors;
import com.library.mappers.AuthorMapper;

import com.library.persistence.AuthorRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDTO> getAllAuthors() {
        // find all results, call toDTO conversion on each, filter out "Unknown"
        Book book = BookFactory.createDefaultBook(AuthorFactory.createDefaultAuthor());
        List<AuthorDTO> authors = new ArrayList<>();
        authorRepository.findAll().stream().forEach(author -> {

            if (!author.getFirstName().equals("Unknown")) {
                AuthorDTO authorDTO = AuthorMapper.toDTO(author);
                book.setAuthor(author);
                authors.add(authorDTO);
            }

        });
        return authors;

    }

    public boolean insertAuthors(List<AuthorDTO> authors) {
        // convert each AuthorDTO to Author entity and save to repository
        authors.stream().map(AuthorMapper::toAuthor).forEach(author -> authorRepository.save(author));
        return true;
    }

    @Transactional
    public int deleteByAuthorName(String firstname, String lastname) {
        int rowsAffected = authorRepository.deleteByFirstNameAndLastName(firstname, lastname);
        return rowsAffected;
    }

    @Transactional
    public void deleteById(UUID id) {
        authorRepository.deleteById(id);
    }

    public UUID findIdByFirstAndLastName(String firstname, String lastname) {
        var author = authorRepository.findByFirstNameAndLastName(firstname, lastname);
        return author.getId();
    }

    @Transactional
    public boolean updateAuthor(AuthorDTO authorDTO) {

        var author = authorRepository.findByFirstNameAndLastName(authorDTO.firstName(),
                authorDTO.lastName());

        if (author != null) {

            author = AuthorMapper.updateFromDTO(authorDTO, author);

            try {
                authorRepository.save(author);
            } catch (Exception ex) {
                log.error(Errors.DATABASE_ERROR + ex.getMessage());
                return false;
            }

        }
        return true;
    }

    @Transactional
    public boolean updateAuthor(UUID id, AuthorDTO authorDTO) {

        var author = authorRepository.findById(id).orElse(null);

        if (author != null) {

            author = AuthorMapper.updateFromDTO(authorDTO, author);

            try {
                authorRepository.save(author);
            } catch (Exception ex) {
                log.error(Errors.DATABASE_ERROR + ex.getMessage());
                return false;
            }

        }
        return true;
    }

}
