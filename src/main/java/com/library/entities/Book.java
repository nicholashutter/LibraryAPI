package com.library.entities;

import java.time.LocalDate;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class Book extends Entity {
    private String title;

    private String author;

    private String isbn;

    private LocalDate publicationDate;

}
