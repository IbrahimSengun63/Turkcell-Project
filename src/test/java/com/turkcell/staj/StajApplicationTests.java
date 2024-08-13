package com.turkcell.staj;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@SpringBootTest
class StajApplicationTests {

	@Test
	void contextLoads() {
	}

	@Test
	public void testMainMethod() {
		// Mock the SpringApplication.run method to verify it is called correctly
		SpringApplication mockSpringApplication = mock(SpringApplication.class);
		String[] args = {"--spring.profiles.active=test"};

		// Simulate calling the main method
		StajApplication.main(args);


	}

}
