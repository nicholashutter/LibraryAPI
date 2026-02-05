package com.library.entities;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "books")
public class Book extends AbstractEntity {

    String title;

    String isbn;

    LocalDate publicationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    Author author;

}
