package com.example.restservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import io.swagger.v3.oas.models.servers.Server;

import io.swagger.v3.oas.models.OpenAPI;

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
