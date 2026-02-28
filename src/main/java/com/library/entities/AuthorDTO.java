package com.library.entities;

import java.util.List;
import java.util.UUID;

public record AuthorDTO(UUID id, String firstName, String lastName, List<BookDTO> books)
{

}
