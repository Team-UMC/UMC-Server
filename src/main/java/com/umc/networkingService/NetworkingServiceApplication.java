package com.umc.networkingService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class NetworkingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NetworkingServiceApplication.class, args);
	}

}
