package com.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
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
								"/booksMenu.html")
						.permitAll()
						.anyRequest().authenticated())
				.formLogin(form -> form.permitAll())
				.httpBasic(org.springframework.security.config.Customizer.withDefaults())
				.csrf((csrf) -> csrf.disable());
		return http.build();
	}
}