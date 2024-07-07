package com.example.test_alfa.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import com.example.test_alfa.service.DogService;

@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000); // Connection timeout
        factory.setReadTimeout(5000); // Read timeout
        return new RestTemplate(factory);
    }

    @Bean
    public RestTemplate restTemplateWithShortTimeout() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(2000); // Connection timeout for sub-breed endpoint
        factory.setReadTimeout(2000); // Read timeout for sub-breed endpoint
        return new RestTemplate(factory);
    }

    // @Bean
    // public DogService dogService(RestTemplate restTemplate) {
    //     return new DogService(restTemplate);
    // }

}
