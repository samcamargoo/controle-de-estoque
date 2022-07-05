package com.sam.estoque;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiControleEstoqueApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiControleEstoqueApplication.class, args);
	}

}
