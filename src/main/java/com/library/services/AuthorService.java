package com.library.services;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.library.entities.AuthorDTO;
import com.library.exceptions.Errors;
import com.library.mappers.AuthorMapper;
import com.library.persistence.AuthorRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorService
{

    private final AuthorRepository authorRepository;


    public AuthorService(AuthorRepository authorRepository)
    {
        this.authorRepository = authorRepository;
    }


    public List<AuthorDTO> getAllAuthors()
    {
        return authorRepository.findAll().stream().map(AuthorMapper::toDTO)
                .filter(author -> !author.firstName().equals("Unknown")).toList();
    }


    public boolean insertAuthors(List<AuthorDTO> authors)
    {

        authors.stream().map(AuthorMapper::toAuthor).forEach(author -> authorRepository.save(author));


        return true;
    }


    @Transactional
    public int deleteByAuthorName(String firstname, String lastname)
    {
        int rowsAffected = authorRepository.deleteByFirstNameAndLastName(firstname, lastname);


        return rowsAffected;
    }


    public AuthorDTO getAuthorById(UUID id)
    {
        return authorRepository.findById(id).map(AuthorMapper::toDTO).orElse(null);
    }


    @Transactional
    public void deleteById(UUID id)
    {
        authorRepository.deleteById(id);
    }


    public UUID findIdByFirstAndLastName(String firstname, String lastname)
    {
        var author = authorRepository.findByFirstNameAndLastName(firstname, lastname);


        if (author == null)
        {
            throw new IllegalArgumentException("Author " + firstname + " " + lastname + " not found");
        }


        return author.getId();
    }


    @Transactional
    public boolean updateAuthor(AuthorDTO authorDTO)
    {

        var author = authorRepository.findByFirstNameAndLastName(authorDTO.firstName(),


                authorDTO.lastName());


        if (author != null)
        {

            author = AuthorMapper.updateFromDTO(authorDTO, author);


            try
            {
                authorRepository.save(author);
            }
            catch (Exception ex)
            {
                log.error(Errors.DATABASE_ERROR + ex.getMessage());


                return false;
            }

        }


        return true;
    }


    @Transactional
    public boolean updateAuthor(UUID id, AuthorDTO authorDTO)
    {

        var author = authorRepository.findById(id).orElse(null);


        if (author != null)
        {

            author = AuthorMapper.updateFromDTO(authorDTO, author);


            try
            {
                authorRepository.save(author);
            }
            catch (Exception ex)
            {
                log.error(Errors.DATABASE_ERROR + ex.getMessage());


                return false;
            }

        }


        return true;
    }

}

