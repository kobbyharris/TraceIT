package com.vehicletrackingsys.api;

import com.vehicletrackingsys.api.config.EnvConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {
	public static void main(String[] args) {
		//.env variables
		System.setProperty("DB_NAME", EnvConfig.get("DB_NAME"));
		System.setProperty("DB_USER", EnvConfig.get("DB_USER"));
		System.setProperty("DB_PASSWORD", EnvConfig.get("DB_PASSWORD"));
		System.setProperty("SECRET_KEY", EnvConfig.get("SECRET_KEY"));

		SpringApplication.run(ApiApplication.class, args);
	}
}
