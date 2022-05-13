package com.eventops;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class EventopsApplication {
	
    @Bean
    public RestTemplate getresttemplate() {
        return new RestTemplate();
    }
	
	
	public static void main(String[] args) {
		SpringApplication.run(EventopsApplication.class, args);

	}
	}



		
	

