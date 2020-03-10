package com.projects.springboot.app;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.projects.springboot.app.controllers.FamilyController;
import com.projects.springboot.app.entity.Family;
import com.projects.springboot.app.entity.Parent;
import com.projects.springboot.app.service.FamilyService;
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


@WebFluxTest(value = FamilyController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser
public class FamilyTest {

  @Autowired
  private WebTestClient webClient;
  
  @MockBean
  private FamilyService familyService;

  private Parent hofTest;
  
  private Family fmTest;
  private Family fmTest2;

  /**
   * This method is created because we need to start with a family Object to use
   * in all tests.
   * 
   * @throws ParseException Using this exception because the date string format
   *                        doesn't work in the default constructor
   */
  @BeforeEach
  public void beforeAll() throws ParseException {
    hofTest = new Parent("M", "Lucio", "Andres",
        "Gonzales","Nice Parent");

    
    fmTest = new Family(hofTest, "Los Gonzales");
    fmTest2 = new Family(hofTest, "Los Gonsáles");
  }
  
  
  @Test
  public void createFamilyIsOkTest() throws Exception {

    when(familyService.create(fmTest)).thenReturn(Mono.just(fmTest));

    webClient.mutateWith(csrf()).post().uri("/api/families")
        .contentType(MediaType.APPLICATION_JSON).body(Mono.just(fmTest), Family.class)
        .exchange().expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk();

  }

  @Test
  public void findOneFamilyIsOkTest() throws Exception {

    when(familyService.findById("9L")).thenReturn(Mono.just(fmTest));

    webClient.mutateWith(csrf()).get().uri("/api/families/9L")
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk().expectBodyList(Family.class);
  }

  @Test
  public void allFamilysIsOkTest() throws Exception {

    fmTest.setFamilyId("9L");
    fmTest2.setFamilyId("10L");
    
    when(familyService.findAll()).thenReturn(Flux.just(fmTest, fmTest2));

    webClient.mutateWith(csrf()).get().uri("/api/families")
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
        .expectStatus().isOk().expectBody().jsonPath("$.length()").isEqualTo("3");
  }

  @Test
  public void updateFamilyIsOkTest() throws Exception {

    fmTest.setFamilyId("9L");
    when(familyService.update(fmTest.getFamilyId(),fmTest2)).thenReturn(Mono.just(fmTest2));
    fmTest2.setFamilyId("9L");

    webClient.mutateWith(csrf()).put().uri("/api/families/{familyId}","9L")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(fmTest),Family.class)
            .exchange()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectStatus().isOk();
  }

  @Test
  public void deleteFamilyIsOkTest() throws Exception {

    fmTest.setFamilyId("9L");

    when(familyService.delete(fmTest.getFamilyId())).thenReturn(Mono.empty());


    webClient.mutateWith(csrf()).delete().uri("/api/families/{familyId}", fmTest.getFamilyId())
    .exchange().expectStatus().isOk();

  }
  
}
