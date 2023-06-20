package com.exprivia.demo;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "Server",
        version = "1.0",
        description = "Server sito scolastico"
    )
)
public class ProgettoExpriviaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgettoExpriviaApplication.class, args);
	}
}
