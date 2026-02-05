package com.library.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.library.entities.Book;

public interface BookRepository extends JpaRepository<Book, UUID> {

}
