package com.tecnotrans.microservice_perfume;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class MicroservicePerfumeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicePerfumeApplication.class, args);
	}

}
