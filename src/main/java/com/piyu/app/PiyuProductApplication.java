package com.piyu.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PiyuProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(PiyuProductApplication.class, args);
	}

}
