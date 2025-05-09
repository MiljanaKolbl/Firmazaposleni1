package com.firma.firmazaposleni1;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Firmazaposleni1Application {

	public static void main(String[] args) {
		SpringApplication.run(Firmazaposleni1Application.class, args);
	}
	@Bean
	public ApplicationRunner testStartup() {
		return args -> {
			System.out.println("✅ Aplikacija je stigla do kraja boot procesa.");
		};
	}
}
