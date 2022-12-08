package com.example.recipository;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RecipositoryApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecipositoryApplication.class, args);
	}

}
