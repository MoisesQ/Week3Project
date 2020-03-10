package com.projects.springboot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;
import reactivefeign.spring.config.EnableReactiveFeignClients;

@EnableEurekaClient
@SpringBootApplication
@EnableFeignClients
@EnableReactiveFeignClients
public class SpringBootSchoolProjectApplication {

  public static void main(String[] args) {
    SpringApplication.run(SpringBootSchoolProjectApplication.class, args);
  }

  @Bean
  WebClient.Builder webClient() {
    return WebClient.builder();
  }

}
