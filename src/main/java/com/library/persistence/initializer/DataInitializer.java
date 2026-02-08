package com.library.persistence.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.library.entities.Author;
import com.library.entities.factories.AuthorFactory;
import com.library.exceptions.Errors;
import com.library.persistence.AuthorRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(AuthorRepository authorRepository) {
        return args -> {
            if (authorRepository.count() == 0) {
                System.out.println("Initializing default data...");

                Author defaultAuthor = AuthorFactory.createDefaultAuthor();

                try {
                    authorRepository.save(defaultAuthor);
                } catch (Exception ex) {
                    log.error(Errors.DATABASE_ERROR + ex.getMessage());
                }

                System.out.println("Default Author and Book registered successfully.");
            }
        };
    }
}
