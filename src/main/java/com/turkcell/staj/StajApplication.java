package com.turkcell.staj;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
@OpenAPIDefinition(info = @Info(title = "Turkcell Sale Project API", version = "1.0", description = "Documentation for Turkcell Sale Project API"))
public class StajApplication {

	public static void main(String[] args) {
		SpringApplication.run(StajApplication.class, args);
	}

}
