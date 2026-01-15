package com.example.restservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.servers.Server;

import io.swagger.v3.oas.models.OpenAPI;

/**
 * Main Spring Boot application class for the REST Service.
 * This application provides user management REST APIs with MyBatis Plus
 * integration,
 * H2 database support, and comprehensive error handling.
 * 
 * Features:
 * - User CRUD operations via REST endpoints
 * - MyBatis Plus ORM framework integration
 * - OpenAPI/Swagger documentation
 * - Data validation and error handling
 * - Security configuration with Basic Auth
 * 
 * @author Application Development Team
 * @since 1.0
 */
@SpringBootApplication
@MapperScan("com.example.restservice.user.mapper")

public class RestserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestserviceApplication.class, args);
	}

	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
				.addServersItem(new Server()
						.url("http://localhost:8080")
						.description("Development Environment"))
				.addServersItem(new Server()
						.url("https://staging-api.example.com")
						.description("Staging Environment"))
				.addServersItem(new Server()
						.url("https://api.example.com")
						.description("Production Environment"));
	}

}
