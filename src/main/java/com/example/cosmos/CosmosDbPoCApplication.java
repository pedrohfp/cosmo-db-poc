package com.example.cosmos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CosmosDbPoCApplication {

	public static void main(String[] args) {
		SpringApplication.run(CosmosDbPoCApplication.class, args);
	}

}
