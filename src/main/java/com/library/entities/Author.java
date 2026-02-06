package com.library.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "author")
public class Author extends AbstractEntity {
    
    String firstName;

    String lastName;

    @OneToMany(mappedBy = "author")
    private List<Book> books; 

}
