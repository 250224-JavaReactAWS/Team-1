package com.Rev.RevStay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.Rev.RevStay.repos")
public class RevStayApplication {

	public static void main(String[] args) {
		SpringApplication.run(RevStayApplication.class, args);
	}

}
