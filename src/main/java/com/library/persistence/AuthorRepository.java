package com.library.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entities.Author;

public interface AuthorRepository extends JpaRepository<Author, UUID> {

}
