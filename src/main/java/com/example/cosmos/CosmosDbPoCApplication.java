package com.example.cosmos;

import com.microsoft.applicationinsights.attach.ApplicationInsights;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CosmosDbPoCApplication {

	public static void main(String[] args) {
      ApplicationInsights.attach();
      SpringApplication.run(CosmosDbPoCApplication.class, args);
	}

}
