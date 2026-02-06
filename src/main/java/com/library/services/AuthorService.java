package com.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.entities.Author;
import com.library.persistence.AuthorRepository;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<Author> getAllAuthors() {
        // get all matching entries
        return authorRepository.findAll();
    }

    public boolean insertAuthors(List<Author> authors) {
        authors.stream().forEach(author -> authorRepository.save(author));
        return true; // we only return true for success
    }

}
