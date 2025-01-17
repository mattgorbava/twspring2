package com.example.twspring2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("com.example.twspring2.database.users.repository")
public class Twspring2Application {

	public static void main(String[] args) {
		SpringApplication.run(Twspring2Application.class, args);
	}

}
