package com.library.entities;

import java.util.List;

public record AuthorDTO(String firstName, String lastName, List<BookDTO> books) {

}
