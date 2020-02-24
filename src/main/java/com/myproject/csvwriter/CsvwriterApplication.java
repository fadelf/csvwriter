package com.myproject.csvwriter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CsvwriterApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsvwriterApplication.class, args);
	}

}
