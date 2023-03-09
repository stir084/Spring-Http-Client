package com.example.springhttpclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;

@SpringBootApplication
public class SpringHttpClientApplication {

	@Bean
	ApplicationRunner init() {
		return args -> {

		};
	}
	public static void main(String[] args) {
		SpringApplication.run(SpringHttpClientApplication.class, args);
	}

}
