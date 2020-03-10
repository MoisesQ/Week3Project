package com.projects.springboot.app;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers.csrf;

import com.projects.springboot.app.controllers.FamilyMemberController;
import com.projects.springboot.app.entity.Family;
import com.projects.springboot.app.entity.FamilyMember;
import com.projects.springboot.app.entity.Parent;
import com.projects.springboot.app.entity.Student;
import com.projects.springboot.app.service.FamilyMemberService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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

@WebFluxTest(value = FamilyMemberController.class)
@ExtendWith(MockitoExtension.class)
@WithMockUser
public class FamilyMemberTest {

  @Autowired
  private WebTestClient webClient;

  @MockBean
  private FamilyMemberService familyMemberService;

  private Parent prTest;
  private Student stTest;
  private Family fmTest;
  
  private FamilyMember fmmTest;
  private FamilyMember fmmTest2;

  /**
   * This method is created because we need to start with a family Object to use
   * in all tests.
   * 
   * @throws ParseException Using this exception because the date string format
   *                        doesn't work in the default constructor
   */
  @BeforeEach
  public void beforeAll() throws ParseException {
    prTest = new Parent("M", "Lucio", "Andres",
        "Gonzales","Nice Parent");
    
    stTest = new Student("M", "Alejandro", "Mateo", "Gonzales", 
        new SimpleDateFormat("yyyy-mm-dd").parse("1994-06-06"),
        "Nice Student");
    
    fmTest = new Family(prTest, "Los Gonzales");
    
    fmmTest = new FamilyMember(fmTest,"Parent",prTest,null);
    fmmTest2 = new FamilyMember(fmTest,"Student",null,stTest);
  }
  
  
  @Test
  public void createFamilyMemberIsOkTest() throws Exception {

    when(familyMemberService.create(fmmTest)).thenReturn(Mono.just(fmmTest));

    webClient.mutateWith(csrf()).post().uri("/api/familyMembers")
        .contentType(MediaType.APPLICATION_JSON).body(Mono.just(fmmTest), FamilyMember.class)
        .exchange().expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk();

  }

  @Test
  public void findOneFamilyMemberIsOkTest() throws Exception {

    when(familyMemberService.findById("11L")).thenReturn(Mono.just(fmmTest));

    webClient.mutateWith(csrf()).get().uri("/api/familyMembers/11L")
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectStatus().isOk().expectBodyList(FamilyMember.class);
  }

  @Test
  public void allFamilyMembersIsOkTest() throws Exception {

    fmmTest.setFamilyMemberId("11L");
    fmmTest2.setFamilyMemberId("12L");
    
    when(familyMemberService.findAll()).thenReturn(Flux.just(fmmTest, fmmTest2));

    webClient.mutateWith(csrf()).get().uri("/api/familyMembers")
        .exchange()
        .expectHeader().contentType(MediaType.APPLICATION_STREAM_JSON_VALUE)
        .expectStatus().isOk().expectBody().jsonPath("$.length()").isEqualTo("5");

  }

  @Test
  public void updateFamilyMemberIsOkTest() throws Exception {

    fmmTest.setFamilyMemberId("11L");
    when(familyMemberService.update(fmmTest.getFamilyMemberId(),fmmTest2))
        .thenReturn(Mono.just(fmmTest2));
    fmmTest2.setFamilyMemberId("11L");

    webClient.mutateWith(csrf()).put().uri("/api/familyMembers/{familyMemberId}","11L")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(fmmTest),FamilyMember.class)
            .exchange()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectStatus().isOk();
  }

  @Test
  public void deleteFamilyMemberIsOkTest() throws Exception {

    fmmTest.setFamilyMemberId("11L");

    when(familyMemberService.delete(fmmTest.getFamilyMemberId())).thenReturn(Mono.empty());


    webClient.mutateWith(csrf())
    .delete().uri("/api/familyMembers/{familyMemberId}", fmmTest.getFamilyMemberId())
    .exchange().expectStatus().isOk();

  }
  
}
