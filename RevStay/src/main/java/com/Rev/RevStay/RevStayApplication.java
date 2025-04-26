package com.Rev.RevStay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Main class for the RevStay application.
 * 
 * This class serves as the entry point for the Spring Boot application. It 
 * initializes and starts the application context.
 * 
 * Annotations:
 * - `@SpringBootApplication`: Combines `@Configuration`, `@EnableAutoConfiguration`, 
 *   and `@ComponentScan` annotations to configure the Spring Boot application.
 * - `@EnableJpaRepositories`: Enables JPA repositories and specifies the base 
 *   package for repository scanning.
 */
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.Rev.RevStay.repos")
public class RevStayApplication {

    /**
     * The main method that serves as the entry point for the application.
     * 
     * @param args Command-line arguments passed to the application.
     */
    public static void main(String[] args) {
        SpringApplication.run(RevStayApplication.class, args);
    }
}
