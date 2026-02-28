package com.library.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.library.entities.AuthorDTO;
import com.library.entities.BookDTO;

import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"spring.security.user.name=testuser",
		"spring.security.user.password=testpass"
})
@Transactional
class LibraryApplicationTests
{

	@LocalServerPort
	private int port;


	private String BOOKS_ENDPOINT;


	private String AUTHORS_ENDPOINT;


	private RestClient restClient;


	private UUID testBookId;


	private UUID testAuthorId;


	@BeforeEach
	void setUp()
	{
		String baseUrl = "http://localhost:" + port;


		BOOKS_ENDPOINT = baseUrl + "/books";


		AUTHORS_ENDPOINT = baseUrl + "/authors";


		restClient = RestClient.builder()
				.baseUrl(baseUrl)
				.defaultHeaders(h -> h.setBasicAuth("testuser", "testpass"))
				.build();


		testAuthorId = UUID.randomUUID();


		testBookId = UUID.randomUUID();
	}


	private void expectStatus(int expectedStatus, org.springframework.http.ResponseEntity<Void> response)
	{
		assertThat(response.getStatusCode().value()).isEqualTo(expectedStatus);
	}


	private BookDTO bookDto(UUID id, String title, String isbn, String date, String firstName, String lastName)
	{
		return new BookDTO(id, title, isbn, date, firstName, lastName);
	}


	@Test
	void shouldCreateBook()
	{
		BookDTO book = bookDto(null, "Test Book", "123-456-789", "2026-01-01", "John", "Doe");


		ResponseEntity<Void> response = restClient.post().uri(BOOKS_ENDPOINT).body(List.of(book))
				.retrieve().toBodilessEntity();


		expectStatus(201, response);
	}


	@Test
	void shouldGetBooks()
	{
		expectStatus(200, restClient.get().uri(BOOKS_ENDPOINT).retrieve().toBodilessEntity());
	}


	@Test
	void shouldUpdateBookById()
	{
		BookDTO book = bookDto(testBookId, "Pride and Prejudice", "978-0141439518", "1813-01-27", "Easton", "Southall");


		ResponseEntity<Void> response = restClient.put()
				.uri(BOOKS_ENDPOINT + "/aedeec53-d6f9-49ab-b745-1fd1f9e97503")
				.body(book).retrieve().toBodilessEntity();


		expectStatus(200, response);
	}


	@Test
	void shouldDeleteBook()
	{
		UUID bookId = restClient.get().uri(BOOKS_ENDPOINT + "/isbn/978-0141321042").retrieve()
				.body(UUID.class);


		ResponseEntity<Void> response = restClient.delete().uri(BOOKS_ENDPOINT + "/{id}", bookId)
				.retrieve().toBodilessEntity();


		expectStatus(204, response);
	}


	@Test
	void shouldGetAuthors()
	{
		expectStatus(200, restClient.get().uri(AUTHORS_ENDPOINT).retrieve().toBodilessEntity());
	}


	@Test
	void shouldCreateAuthor()
	{
		BookDTO book = bookDto(null, "Test", "100-0-99-702549-1", "2026-01-01", "Test", "Author");


		AuthorDTO author = new AuthorDTO(null, "Test", "Author", List.of(book));


		ResponseEntity<Void> response = restClient.post().uri(AUTHORS_ENDPOINT).body(List.of(author))
				.retrieve().toBodilessEntity();


		expectStatus(201, response);
	}


	@Test
	void shouldUpdateAuthorById()
	{
		UUID bookId = UUID.randomUUID();


		UUID authorId = UUID.randomUUID();


		BookDTO book = bookDto(bookId, "The Hobbit", "9780544003415", "1937-09-21", "J.R.R.", "Tolkien");


		AuthorDTO author = new AuthorDTO(authorId, "J.R.R.", "Tolkien", List.of(book));


		ResponseEntity<Void> response = restClient.put().uri(AUTHORS_ENDPOINT + "/" + authorId)
				.body(author).retrieve().toBodilessEntity();


		expectStatus(200, response);
	}


	@Test
	void shouldDeleteAuthor()
	{
		UUID authorId = restClient.get().uri(AUTHORS_ENDPOINT + "/Jane/Austen").retrieve()
				.body(UUID.class);


		ResponseEntity<Void> response = restClient.delete().uri(AUTHORS_ENDPOINT + "/{id}", authorId)
				.retrieve().toBodilessEntity();


		expectStatus(204, response);
	}


	@Test
	void shouldCreateAuthorWithBooksAndVerifyPersistence()
	{
		BookDTO book = bookDto(null, "Journey to the West", "9781607446552", "1592-01-01", "Cheng'en", "Wu");


		AuthorDTO author = new AuthorDTO(null, "Cheng'en", "Wu", List.of(book));


		expectStatus(201, restClient.post().uri(AUTHORS_ENDPOINT).body(List.of(author))
				.retrieve().toBodilessEntity());


		List<AuthorDTO> authors = restClient.get().uri(AUTHORS_ENDPOINT).retrieve()
				.body(new ParameterizedTypeReference<List<AuthorDTO>>()
				{
				});


		AuthorDTO savedAuthor = authors.stream()
				.filter(a -> "Wu".equals(a.lastName()))
				.findFirst()
				.orElseThrow(() -> new AssertionError("Author not found"));


		assertThat(savedAuthor.books()).isNotEmpty();


		assertThat(savedAuthor.books().get(0).isbn()).isEqualTo("9781607446552");


		assertThat(savedAuthor.books().get(0).title()).isEqualTo("Journey to the West");
	}

}
