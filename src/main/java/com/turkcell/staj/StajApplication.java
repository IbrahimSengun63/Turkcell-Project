package com.turkcell.staj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class StajApplication {

	public static void main(String[] args) {
		SpringApplication.run(StajApplication.class, args);
	}

}
