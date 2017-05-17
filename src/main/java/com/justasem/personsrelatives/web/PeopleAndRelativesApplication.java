package com.justasem.personsrelatives.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan
public class PeopleAndRelativesApplication {

	public static void main(String[] args) {
		SpringApplication.run(PeopleAndRelativesApplication.class, args);
	}
}
