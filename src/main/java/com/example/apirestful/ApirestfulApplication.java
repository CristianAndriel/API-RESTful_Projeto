package com.example.apirestful;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ApirestfulApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ApirestfulApplication.class, args);

		String port = context.getEnvironment().getProperty("server.port", "8080");
		System.out.println("\n========================================");
		System.out.println("iniciado com sucesso!");
		System.out.println("========================================");
		System.out.println("Swagger UI: http://localhost:" + port + "/swagger-ui/index.html");
		System.out.println("API Docs: http://localhost:" + port + "/v3/api-docs");
		System.out.println("========================================\n");
	}

}
