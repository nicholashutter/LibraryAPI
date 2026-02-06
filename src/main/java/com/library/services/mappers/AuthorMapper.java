package com.library.services.mappers;

import org.springframework.stereotype.Component;


import com.library.entities.Author;
import com.library.entities.AuthorDTO;
import com.library.exceptions.ApplicationException;
import com.library.exceptions.Errors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthorMapper {

    public static AuthorDTO toDTO(Author author) {

        AuthorDTO dto = null; 

        try
        {
            if (author == null) {
            throw new ApplicationException(Errors.NULL_ARGUMENT);
        }

        dto = new AuthorDTO(author.getFirstName(), author.getLastName(),
                author.getBooks().stream().map(book -> book.getTitle()).toArray(size -> new String[size]));

        
        }
        catch (ApplicationException ex)
        {
            log.error(ex.getMessage());
        }

        return dto;
        
    }

}
