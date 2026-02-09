package com.library.persistence;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entities.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {

    int deleteByTitle(String title);

    Book findByTitle(String title);
    Book findIdByisbn(String isbn);
    void deleteById(UUID id);

    Optional<Book> findById(UUID id);

}
