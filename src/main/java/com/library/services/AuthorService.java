package com.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.entities.Author;
import com.library.entities.AuthorDTO;
import com.library.persistence.AuthorRepository;
import com.library.services.mappers.AuthorMapper;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDTO> getAllAuthors() {
        // get all matching entries
        return authorRepository.findAll().stream().map(AuthorMapper::toDTO).toList();
    }

    public boolean insertAuthors(List<Author> authors) {
        authors.stream().forEach(author -> authorRepository.save(author));
        return true; // we only return true for success
    }

}
