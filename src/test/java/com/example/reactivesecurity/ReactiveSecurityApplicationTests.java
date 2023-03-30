package com.example.reactivesecurity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReactiveSecurityApplicationTests {

	@Autowired
	private WebTestClient webTestClient;

	@Test
	@WithMockUser("testuser")
	void contextLoads() {

		webTestClient.get()
				.uri("/")
				.exchange()
				.expectStatus().isOk()
				.expectBody(String.class).isEqualTo("Hello, testuser");
	}
	@Test
	void shouldRedirectToLoginPage() {

		webTestClient.get()
				.uri("/")
				.exchange()
				.expectStatus().is3xxRedirection();
	}

	@Test
	void shouldPreAuthenticate() {

		webTestClient.get()
				.uri("/")
				.header("USER", "user1")
				.exchange()
				.expectStatus().is3xxRedirection()
				.expectHeader().exists("Location")
				.expectHeader().valueEquals("Location", "/");
	}

}
