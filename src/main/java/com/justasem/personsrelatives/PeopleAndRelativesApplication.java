package com.justasem.personsrelatives;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

@EntityScan(basePackageClasses = {PeopleAndRelativesApplication.class, Jsr310JpaConverters.class})
@SpringBootApplication
public class PeopleAndRelativesApplication extends SpringBootServletInitializer{

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PeopleAndRelativesApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(PeopleAndRelativesApplication.class, args);
	}
}
