package com.example.springhttpclient;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Component
public class Config {
    @Bean
    SpringHttpClientApplication.RepositoryService repositoryService(){
        WebClient client = WebClient.create("https://api.worldbank.org");
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builder(WebClientAdapter.forClient(client)).build();
        SpringHttpClientApplication.RepositoryService service = factory.createClient(SpringHttpClientApplication.RepositoryService.class);
        return service;
    }
}
