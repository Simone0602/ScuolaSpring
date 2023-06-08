package com.exprivia.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@SpringBootApplication
@EnableAutoConfiguration
@OpenAPIDefinition(
    info = @Info(
        title = "Sito scolastico",
        version = "1.0",
        description = "Progetto di prova per la creazione di un server utilizzato dal frontend per la comunicazione tra client e database"
    )
)
public class ProgettoExpriviaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProgettoExpriviaApplication.class, args);
	}
}
