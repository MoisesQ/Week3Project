package com.projects.springboot.app;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.projects.springboot.app.controllers.ParentController;
import com.projects.springboot.app.entity.Parent;
import com.projects.springboot.app.service.ParentService;
import java.text.ParseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(value = ParentController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser
public class ParentTest {

  @Autowired
  private WebTestClient webClient;
  
  @MockBean
  private ParentService parentService;

  private Parent prTest;
  private Parent prTest2;

  /**
   * This method is created because we need to start with a parent Object to use
   * in all tests.
   * 
   * @throws ParseException Using this exception because the date string format
   *                        doesn't work in the default constructor
   */
  @BeforeEach
  public void beforeAll() throws ParseException {
    prTest = new Parent("M", "Lucio", "Andres", "Gonzales", "Nice Parent");
    prTest2 =  new Parent("M", "Mariano", "Luis", "Gonzales","Nice Parent");
  }
  
  
  @Test
  public void createParentIsOkTest() throws Exception {

    when(parentService.create(prTest)).thenReturn(Mono.just(prTest));

    webClient.mutateWith(csrf()).post().uri("/api/parents")
        .contentType(MediaType.APPLICATION_JSON).body(Mono.just(prTest), Parent.class)
        .exchange().expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk();

  }

  @Test
  public void findOneParentIsOkTest() throws Exception {

    when(parentService.findById("7L")).thenReturn(Mono.just(prTest));

    webClient.mutateWith(csrf()).get().uri("/api/parents/7L")
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk().expectBodyList(Parent.class);
  }

  @Test
  public void allParentsIsOkTest() throws Exception {

    prTest.setParentId("7L");
    prTest2.setParentId("8L");
    
    when(parentService.findAll()).thenReturn(Flux.just(prTest, prTest2));

    webClient.mutateWith(csrf()).get().uri("/api/parents")
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
        .expectStatus().isOk().expectBody().jsonPath("$.length()").isEqualTo("7");

  }

  @Test
  public void updateParentIsOkTest() throws Exception {

    prTest.setParentId("7L");
    when(parentService.update(prTest.getParentId(),prTest2)).thenReturn(Mono.just(prTest2));
    prTest2.setParentId("7L");

    webClient.mutateWith(csrf()).put().uri("/api/parents/{parentId}","7L")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(prTest),Parent.class)
            .exchange()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectStatus().isOk();
  }

  @Test
  public void deleteParentIsOkTest() throws Exception {

    prTest.setParentId("7L");

    when(parentService.delete(prTest.getParentId())).thenReturn(Mono.empty());


    webClient.mutateWith(csrf()).delete().uri("/api/parents/{parentId}", prTest.getParentId())
    .exchange().expectStatus().isOk();

  }

  
}
