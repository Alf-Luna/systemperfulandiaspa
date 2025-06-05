package com.tecnotrans.microservice_sale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class MicroserviceSaleApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceSaleApplication.class, args);
	}

}
