package com.library.entities;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Entity;

import jakarta.persistence.CascadeType;
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

    @ManyToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
    Author author;

}
