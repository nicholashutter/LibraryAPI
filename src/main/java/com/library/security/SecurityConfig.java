package com.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.authorizeHttpRequests(auth -> auth
						.requestMatchers(
								"/", "/index.html", "/viewAuthors.html", "/viewBooks.html", "/authorsMenu.html",
								"/booksMenu.html", "/error") // Added /error to permitAll
						.permitAll()
						.anyRequest().authenticated())
				// SESSION MANAGEMENT: Helps handle stale sessions after restart
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
						.invalidSessionUrl("/")) // Redirects to home if session is stale
				.formLogin(form -> form.permitAll())
				.httpBasic(org.springframework.security.config.Customizer.withDefaults())
				.csrf((csrf) -> csrf.disable());

		return http.build();
	}
}