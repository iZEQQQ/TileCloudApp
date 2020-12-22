package com.example.demo.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//Provider konfiguracji
@Configuration
public class RestTemplateProvider {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
