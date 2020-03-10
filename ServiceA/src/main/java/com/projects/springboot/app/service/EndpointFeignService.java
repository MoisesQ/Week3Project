package com.projects.springboot.app.service;

import com.projects.springboot.app.config.FeignServiceConfig;
import com.projects.springboot.app.entity.Student;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;


@ReactiveFeignClient(name = "service-b", configuration = FeignServiceConfig.class)
public interface EndpointFeignService {
  @CircuitBreaker(name = "service-b", fallbackMethod = "fallback_CB")
  @Retry(name = "service-b", fallbackMethod = "fallback_retry")
  @GetMapping(path = "/api/students/special-endpoint",
          produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
  public Flux<Student> getSpecificStudents(@RequestParam(value = "studentIds", required = true) 
  List<String> studentIds);

  default Flux<Student> fallback_CB(List<String> studentIds, TimeoutException e) {
    Student student = new Student();
    return Flux.just(student);
  }

  default Flux<Student> fallback_retry(List<String> studentIds, CallNotPermittedException e) {
    return Flux.just(new Student());
  }
}
