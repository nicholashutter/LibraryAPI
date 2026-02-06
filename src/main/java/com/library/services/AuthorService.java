package com.library.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.library.entities.AuthorDTO;
import com.library.mappers.AuthorMapper;
import com.library.persistence.AuthorRepository;

@Service
public class AuthorService {
    private final AuthorRepository authorRepository;

    public AuthorService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    public List<AuthorDTO> getAllAuthors() {
        // find all results, call toDTO conversion on each, filter out "Unknown"
        // authors, return list
        return authorRepository.findAll().stream().map(AuthorMapper::toDTO)
                .filter(author -> !author.firstName().equals("Unknown")).toList();
    }

    public boolean insertAuthors(List<AuthorDTO> authors) {
        // convert each AuthorDTO to Author entity and save to repository
        authors.stream().map(AuthorMapper::toAuthor).forEach(author -> authorRepository.save(author));
        return true;
    }

}
