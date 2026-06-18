package com.aula.s3.s3exemplo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class S3exemploApplication {

	public static void main(String[] args) {
		SpringApplication.run(S3exemploApplication.class, args);
	}

}
