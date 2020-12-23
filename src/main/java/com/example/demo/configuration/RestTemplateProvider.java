package com.example.demo.configuration;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

//configuration provider
@Configuration
public class RestTemplateProvider {

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}
