package com.library.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClient;

import com.library.entities.Author;
import com.library.entities.AuthorDTO;
import com.library.entities.Book;
import com.library.entities.BookDTO;
import com.library.entities.factories.AuthorFactory;
import com.library.entities.factories.BookFactory;
import com.library.mappers.AuthorMapper;
import com.library.mappers.BookMapper;

import jakarta.transaction.Transactional;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = {
		"spring.security.user.name=testuser",
		"spring.security.user.password=testpass"
})
@Transactional
class LibraryApplicationTests {
	@LocalServerPort
	private int port;

	private String BASE_URL;
	private String BOOKS_ENDPOINT;
	private String AUTHORS_ENDPOINT;

	private Book book;
	private BookDTO bookDTO;
	private Author author;
	private AuthorDTO authorDTO;
	private RestClient restClient;

	@BeforeEach
	void setUp() {

		this.BASE_URL = "http://localhost:";
		this.BOOKS_ENDPOINT = BASE_URL + port + "/books";
		this.AUTHORS_ENDPOINT = BASE_URL + port + "/authors";

		restClient = RestClient.builder()
				.baseUrl(BASE_URL + port)
				.defaultHeaders(h -> h.setBasicAuth("testuser", "testpass"))
				.build();

		Random random = new Random();

		int threeDigitRandom = 100 + random.nextInt(900);

		this.author = AuthorFactory.createAuthor(String.format("%d", threeDigitRandom),
				String.format("%d", threeDigitRandom),
				new ArrayList<>(), LocalDate.now(), LocalDate.now());
		this.book = BookFactory.createBook(String.format("%d", threeDigitRandom),
				this.author, String.format("%d-0-99-702549-1", threeDigitRandom), LocalDate.now(), LocalDate.now(),
				LocalDate.now());

		this.authorDTO = AuthorMapper.toDTO(this.author);
		this.bookDTO = BookMapper.toDTO(this.book, this.author);
	}

	@Test
	void shouldCreateBook() {

		ResponseEntity<Void> postResponse = restClient.post()
				.uri(BOOKS_ENDPOINT)
				.body(List.of(this.bookDTO))
				.retrieve()
				.toBodilessEntity();

		assertThat(postResponse.getStatusCode().value()).isEqualTo(201);

	}

	@Test
	void shouldGetBooks() {

		ResponseEntity<Void> response = restClient.get()
				.uri(BOOKS_ENDPOINT)
				.retrieve()
				.toBodilessEntity();

		assertThat(response.getStatusCode().value()).isEqualTo(200);

	}

	@Test
	void shouldGetBookIdByISBN() {

		ResponseEntity<UUID> response = restClient.get()
				// pride and prejudice isbn
				.uri(BOOKS_ENDPOINT + "/978-0141439518")
				.retrieve()
				.toEntity(UUID.class);

		assertThat(response.getStatusCode().value()).isEqualTo(200);
	}

	@Test
	void shouldUpdateBookById() {

		BookDTO knownWorkingValue = new BookDTO("Pride and Prejudice", "978-0141439518", "1813-01-27", "Easton",
				"Southall");

		ResponseEntity<Void> response = restClient.put()
				// pride and prejudice id
				.uri(BOOKS_ENDPOINT + "/aedeec53-d6f9-49ab-b745-1fd1f9e97503")
				.body(knownWorkingValue)
				.retrieve()
				.toBodilessEntity();

		assertThat(response.getStatusCode().value()).isEqualTo(200);
	}

	@Test
	void shouldUpdateBook() {
		BookDTO PrideAndPrejudice = new BookDTO("Pride and Prejudice", "978-0141439518", "1813-01-27", "Easton",
				"Southall");

		ResponseEntity<Void> response = restClient.put()
				// pride and prejudice id
				.uri(BOOKS_ENDPOINT)
				.body(PrideAndPrejudice)
				.retrieve()
				.toBodilessEntity();

		assertThat(response.getStatusCode().value()).isEqualTo(200);
	}

	@Test
	void shouldDeleteBook() {
		BookDTO TheCallOfTheWild = new BookDTO("The Call of the Wild", "978-0141321042", "1903-01-01", "Jack",
				"London");

		ResponseEntity<Void> response = restClient.method(org.springframework.http.HttpMethod.DELETE)
				.uri(BOOKS_ENDPOINT)
				.body(TheCallOfTheWild)
				.retrieve()
				.toBodilessEntity();

		assertThat(response.getStatusCode().value()).isEqualTo(204);
	}

	@Test
	void shouldGetAuthors() {

		ResponseEntity<Void> response = restClient.get()
				.uri(AUTHORS_ENDPOINT)
				.retrieve()
				.toBodilessEntity();

		assertThat(response.getStatusCode().value()).isEqualTo(200);

	}

	@Test
	void shouldCreateAuthor() {
		ResponseEntity<Void> postResponse = restClient.post()
				.uri(AUTHORS_ENDPOINT)
				.body(List.of(this.authorDTO))
				.retrieve()
				.toBodilessEntity();

		assertThat(postResponse.getStatusCode().value()).isEqualTo(201);
	}

	@Test
	void shouldUpdateAuthorById() {

		String fixedIsbn = "9780544003415";

		BookDTO book = new BookDTO(
				"The Hobbit",
				fixedIsbn,
				"1937-09-21",
				"J.R.R.",
				"Tolkien");

		AuthorDTO updatedAuthor = new AuthorDTO("J.R.R.", "Tolkien", List.of(book));

		ResponseEntity<Void> response = restClient.put()
				.uri(AUTHORS_ENDPOINT + "/a2222222-2222-2222-2222-222222222222")
				.body(updatedAuthor)
				.retrieve()
				.toBodilessEntity();

		assertThat(response.getStatusCode().value()).isEqualTo(200);
	}

	@Test
	void shouldUpdateAuthor() {

		String fixedIsbn = "9780544003415";

		BookDTO firstBook = new BookDTO(
				"Emma",
				fixedIsbn,
				"1815-12-23",
				"Jane",
				"Austen");

		List<BookDTO> books = List.of(firstBook);

		AuthorDTO updatedAuthor = new AuthorDTO("Jane", "Austen", books);

		ResponseEntity<Void> response = restClient.put()
				.uri(AUTHORS_ENDPOINT)
				.body(updatedAuthor)
				.retrieve()
				.toBodilessEntity();

		assertThat(response.getStatusCode().value()).isEqualTo(200);
	}

	@Test
	void shouldDeleteAuthor() {
		String fixedIsbn = "9780141439518";
		BookDTO book = new BookDTO(
				"Pride and Prejudice",
				fixedIsbn,
				"1813-01-28",
				"Jane",
				"Austen");

		AuthorDTO janeAusten = new AuthorDTO("Jane", "Austen", List.of(book));

		ResponseEntity<Void> response = restClient.method(org.springframework.http.HttpMethod.DELETE)
				.uri(AUTHORS_ENDPOINT)
				.body(janeAusten)
				.retrieve()
				.toBodilessEntity();

		assertThat(response.getStatusCode().value()).isEqualTo(200);
	}

	@Test
	void shouldCreateAuthorWithBooksAndVerifyPersistence() {
		String journeyIsbn = "9781607446552";

		BookDTO journeyBook = new BookDTO(
				"Journey to the West",
				journeyIsbn,
				"1592-01-01",
				"Cheng'en",
				"Wu");

		AuthorDTO newAuthorDTO = new AuthorDTO("Cheng'en", "Wu", List.of(journeyBook));

		ResponseEntity<Void> postResponse = restClient.post()
				.uri(AUTHORS_ENDPOINT)
				.body(List.of(newAuthorDTO))
				.retrieve()
				.toBodilessEntity();

		assertThat(postResponse.getStatusCode().value()).isEqualTo(201);

		List<AuthorDTO> authors = restClient.get()
				.uri(AUTHORS_ENDPOINT)
				.retrieve()
				.body(new ParameterizedTypeReference<List<AuthorDTO>>() {
				});

		AuthorDTO savedAuthor = authors.stream()
				.filter(a -> "Wu".equals(a.lastName()))
				.findFirst()
				.orElseThrow(() -> new AssertionError("Author not found in database"));

		assertThat(savedAuthor.bookTitles()).isNotEmpty();
		assertThat(savedAuthor.bookTitles().get(0).isbn()).isEqualTo(journeyIsbn);
		assertThat(savedAuthor.bookTitles().get(0).title()).isEqualTo("Journey to the West");
	}

}