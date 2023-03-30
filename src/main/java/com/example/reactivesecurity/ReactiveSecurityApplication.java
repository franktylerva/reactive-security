package com.example.reactivesecurity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@SpringBootApplication
@RestController
public class ReactiveSecurityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReactiveSecurityApplication.class, args);
	}

	@GetMapping("/")
	public Mono<String> hello(Authentication authentication) {
		return Mono.just("Hello, " + authentication.getName());
	}

}
