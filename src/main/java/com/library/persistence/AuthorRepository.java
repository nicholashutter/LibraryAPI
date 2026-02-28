package com.library.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, UUID> {

    int deleteByFirstNameAndLastName(String firstName, String lastName);

    Author findByFirstNameAndLastName(String firstName, String lastName);

    UUID findIdByFirstNameAndLastName(String firstName, String lastName);

}
