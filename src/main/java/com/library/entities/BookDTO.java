package com.library.entities;

import java.util.UUID;

public record BookDTO(UUID id, String title, String isbn, String publicationDate, String firstName, String lastName)
{

}
